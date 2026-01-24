package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryStatusRepository extends JpaRepository<DeliveryStatus, Long> {
    Optional<DeliveryStatus> findByName(String name);
}
