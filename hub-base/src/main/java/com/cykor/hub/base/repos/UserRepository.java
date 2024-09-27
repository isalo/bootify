package com.cykor.hub.base.repos;

import com.cykor.hub.base.domain.Congregation;
import com.cykor.hub.base.domain.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, UUID> {

    @EntityGraph(attributePaths = "roles")
    User findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    User findFirstByCongregations(Congregation congregation);

    List<User> findAllByCongregations(Congregation congregation);

}
