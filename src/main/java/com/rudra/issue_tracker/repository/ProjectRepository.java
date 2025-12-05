package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByCreatedBy(Long userId);
}
