package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.IssueType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IssueTypeRepository extends JpaRepository<IssueType, Integer> {
    Optional<IssueType> findByName(String name);
}

