package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.Project;
import com.rudra.issue_tracker.model.ProjectTeam;
import com.rudra.issue_tracker.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectTeamRepository extends JpaRepository<ProjectTeam, Long> {
    List<ProjectTeam> findByProject(Project project);
    Optional<ProjectTeam> findByProjectAndTeam(Project project, Team team);
}
