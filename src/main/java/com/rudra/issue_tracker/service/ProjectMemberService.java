package com.rudra.issue_tracker.service;

import com.rudra.issue_tracker.exceptions.NotFoundException;
import com.rudra.issue_tracker.model.Project;
import com.rudra.issue_tracker.model.ProjectMember;
import com.rudra.issue_tracker.model.ProjectRole;
import com.rudra.issue_tracker.model.User;
import com.rudra.issue_tracker.repository.ProjectMemberRepository;
import com.rudra.issue_tracker.repository.ProjectRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRoleRepository projectRoleRepository;

    @Transactional
    public ProjectMember addMember(Project project, User user, String roleName) {
        ProjectRole role = projectRoleRepository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException("Role not found: " + roleName));

        // avoid duplicates
        projectMemberRepository.findByProjectAndUser(project, user)
                .ifPresent(pm -> {
                    throw new IllegalArgumentException("User already in project");
                });

        ProjectMember member = ProjectMember.builder()
                .project(project)
                .user(user)
                .role(role)
                .build();

        return projectMemberRepository.save(member);
    }
}
