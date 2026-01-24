// SprintRepository.java
package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.Project;
import com.rudra.issue_tracker.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {
    List<Sprint> findByProject(Project project);
    List<Sprint> findByProjectAndStatus(Project project, String status);
}
