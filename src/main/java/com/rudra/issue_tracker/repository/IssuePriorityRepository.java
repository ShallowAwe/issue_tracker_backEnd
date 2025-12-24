package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.IssuePriority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IssuePriorityRepository extends JpaRepository<IssuePriority, Integer> {
    Optional<IssuePriority> findByName(String name);
}
