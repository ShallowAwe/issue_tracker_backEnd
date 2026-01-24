package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.ConversationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationStatusRepository extends JpaRepository<ConversationStatus, Long> {
    Optional<ConversationStatus> findByName(String name);
}
