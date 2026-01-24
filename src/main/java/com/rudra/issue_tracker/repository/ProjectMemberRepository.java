// ProjectMemberRepository.java
package com.rudra.issue_tracker.repository;

import com.rudra.issue_tracker.model.Project;
import com.rudra.issue_tracker.model.ProjectMember;
import com.rudra.issue_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    List<ProjectMember> findByProject(Project project);
    Optional<ProjectMember> findByProjectAndUser(Project project, User user);
}
