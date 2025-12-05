package com.rudra.issue_tracker.repository;
import com.rudra.issue_tracker.model.ProjectTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectTeamRepository extends JpaRepository<ProjectTeam, Long> {

    Optional<ProjectTeam> findByProjectIdAndTeamId(Long projectId, Long teamId);

    List<ProjectTeam> findByProjectId(Long projectId);

    List<ProjectTeam> findByTeamId(Long teamId);

    boolean existsByProjectIdAndTeamId(Long projectId, Long teamId);
}
