package com.cykor.hub.base.repos;

import com.cykor.hub.base.domain.LoginHistory;
import com.cykor.hub.base.domain.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LoginHistoryRepository extends JpaRepository<LoginHistory, UUID> {

    LoginHistory findFirstByUser(User user);

}
