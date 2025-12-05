package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.IssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IssueStatusRepository extends JpaRepository<IssueStatus, Long> {

    Optional<IssueStatus> findByName(String name);
}
