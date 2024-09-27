package com.cykor.hub.base.service;

import com.cykor.hub.base.domain.LoginHistory;
import com.cykor.hub.base.domain.User;
import com.cykor.hub.base.model.UserDTO;
import com.cykor.hub.base.repos.CongregationRepository;
import com.cykor.hub.base.repos.LoginHistoryRepository;
import com.cykor.hub.base.repos.RoleRepository;
import com.cykor.hub.base.repos.UserRepository;
import com.cykor.hub.base.util.NotFoundException;
import com.cykor.hub.base.util.ReferencedWarning;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CongregationRepository congregationRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final LoginHistoryRepository loginHistoryRepository;

    public UserServiceImpl(final UserRepository userRepository, final RoleRepository roleRepository,
            final CongregationRepository congregationRepository,
            final PasswordEncoder passwordEncoder, final UserMapper userMapper,
            final LoginHistoryRepository loginHistoryRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.congregationRepository = congregationRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.loginHistoryRepository = loginHistoryRepository;
    }

    @Override
    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> userMapper.updateUserDTO(user, new UserDTO()))
                .toList();
    }

    @Override
    public UserDTO get(final UUID id) {
        return userRepository.findById(id)
                .map(user -> userMapper.updateUserDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public UUID create(final UserDTO userDTO) {
        final User user = new User();
        userMapper.updateUser(userDTO, user, roleRepository, congregationRepository, passwordEncoder);
        return userRepository.save(user).getId();
    }

    @Override
    public void update(final UUID id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        userMapper.updateUser(userDTO, user, roleRepository, congregationRepository, passwordEncoder);
        userRepository.save(user);
    }

    @Override
    public void delete(final UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final LoginHistory userLoginHistory = loginHistoryRepository.findFirstByUser(user);
        if (userLoginHistory != null) {
            referencedWarning.setKey("user.loginHistory.user.referenced");
            referencedWarning.addParam(userLoginHistory.getId());
            return referencedWarning;
        }
        return null;
    }

}
