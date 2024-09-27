package com.cykor.hub.base.service;

import com.cykor.hub.base.domain.User;
import com.cykor.hub.base.model.JwtUserDetails;
import com.cykor.hub.base.repos.UserRepository;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public JwtUserDetailsService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public JwtUserDetails loadUserByUsername(final String username) {
        final User user = userRepository.findByEmailIgnoreCase(username);
        if (user == null) {
            log.warn("user not found: {}", username);
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        final List<SimpleGrantedAuthority> authorities = user.getRoles() == null ? Collections.emptyList() : 
                user.getRoles()
                .stream()
                .map(roleRef -> new SimpleGrantedAuthority(roleRef.getName()))
                .toList();
        return new JwtUserDetails(user.getId(), username, user.getPassword(), authorities);
    }

}
