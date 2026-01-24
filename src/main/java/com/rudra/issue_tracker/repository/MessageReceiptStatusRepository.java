package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.MessageReceiptStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageReceiptStatusRepository extends JpaRepository<MessageReceiptStatus, Long> {
    Optional<MessageReceiptStatus> findByName(String name);
}
