package com.cykor.hub.base.repos;

import com.cykor.hub.base.domain.Role;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, UUID> {

    Role findByName(String name);

}
