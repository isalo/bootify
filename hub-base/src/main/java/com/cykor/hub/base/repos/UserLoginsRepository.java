package com.cykor.hub.base.repos;

import com.cykor.hub.base.domain.User;
import com.cykor.hub.base.domain.UserLogins;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserLoginsRepository extends JpaRepository<UserLogins, UUID> {

    UserLogins findFirstByUser(User user);

}
