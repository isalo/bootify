package com.cykor.hub.base.service;

import com.cykor.hub.base.domain.User;
import com.cykor.hub.base.model.RegistrationRequest;
import com.cykor.hub.base.repos.RoleRepository;
import com.cykor.hub.base.repos.UserRepository;
import com.cykor.hub.base.util.UserRoles;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class RegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public RegistrationService(final UserRepository userRepository,
            final PasswordEncoder passwordEncoder, final RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public void register(final RegistrationRequest registrationRequest) {
        log.info("registering new user: {}", registrationRequest.getEmail());

        final User user = new User();
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setResetPasswordToken(registrationRequest.getResetPasswordToken());
        user.setResetPasswordTokenSentAt(registrationRequest.getResetPasswordTokenSentAt());
        user.setEmailConfirmationToken(registrationRequest.getEmailConfirmationToken());
        user.setEmailConfirmationTokenSentAt(registrationRequest.getEmailConfirmationTokenSentAt());
        user.setEmailConfirmed(registrationRequest.getEmailConfirmed());
        user.setCountryCode(registrationRequest.getCountryCode());
        user.setPhone(registrationRequest.getPhone());
        user.setPhoneConfirmationToken(registrationRequest.getPhoneConfirmationToken());
        user.setPhoneConfirmationTokenSentAt(registrationRequest.getPhoneConfirmationTokenSentAt());
        user.setPhoneConfirmed(registrationRequest.getPhoneConfirmed());
        user.setProfileImageUrl(registrationRequest.getProfileImageUrl());
        user.setIsActive(registrationRequest.getIsActive());
        user.setIsLocked(registrationRequest.getIsLocked());
        // assign default role
        user.setRoles(Collections.singleton(roleRepository.findByName(UserRoles.ROLE_ADMIN)));
        userRepository.save(user);
    }

}
