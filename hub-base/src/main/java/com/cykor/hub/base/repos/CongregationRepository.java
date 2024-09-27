package com.cykor.hub.base.repos;

import com.cykor.hub.base.domain.Congregation;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CongregationRepository extends JpaRepository<Congregation, UUID> {
}
