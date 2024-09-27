package com.cykor.hub.base.config;

import com.cykor.hub.base.domain.Role;
import com.cykor.hub.base.repos.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class RoleLoader implements ApplicationRunner {

    private final RoleRepository roleRepository;

    public RoleLoader(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(final ApplicationArguments args) {
        if (roleRepository.count() != 0) {
            return;
        }
        log.info("initializing roles");
        final Role roleAdminRole = new Role();
        roleAdminRole.setName("ROLE_ADMIN");
        roleRepository.save(roleAdminRole);
        final Role roleUserRole = new Role();
        roleUserRole.setName("ROLE_USER");
        roleRepository.save(roleUserRole);
    }

}
