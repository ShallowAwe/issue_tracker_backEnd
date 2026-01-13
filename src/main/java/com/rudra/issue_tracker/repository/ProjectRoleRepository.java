package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRoleRepository extends JpaRepository<ProjectRole, Long> {
    Optional<ProjectRole> findByName(String name);
    boolean existsByName(String name);

}
