// ProjectRepository.java
package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByKey(String key);
    boolean existsByKey(String key);
    Optional<Project> findByOwnerId(Long ownerId);
}
