package com.cykor.hub.base.repos;

import com.cykor.hub.base.domain.Privilege;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PrivilegeRepository extends JpaRepository<Privilege, UUID> {
}
