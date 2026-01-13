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

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRoleRepository projectRoleRepository;
    private final ProjectService projectService;
    private final UserService userService;

    @Transactional
    public ProjectMember addMember(String projectKey, Long userId, String roleName) {
        Project project = projectService.getByKey(projectKey);
        User user = userService.findById(userId);
        ProjectRole role = projectRoleRepository.findByName(roleName)
                .orElseThrow(() -> new NotFoundException("Role not found: " + roleName));

        // Check if already member
        projectMemberRepository.findByProjectAndUser(project, user)
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("User already member of project");
                });

        ProjectMember member = ProjectMember.builder()
                .project(project)
                .user(user)
                .role(role)
                .build();

        return projectMemberRepository.save(member);
    }

    @Transactional
    public ProjectMember updateRole(String projectKey, Long userId, String newRoleName) {
        ProjectMember member = findByProjectKeyAndUserId(projectKey, userId);
        ProjectRole role = projectRoleRepository.findByName(newRoleName)
                .orElseThrow(() -> new NotFoundException("Role not found: " + newRoleName));

        member.setRole(role);
        return projectMemberRepository.save(member);
    }

    @Transactional
    public void removeMember(String projectKey, Long userId) {
        ProjectMember member = findByProjectKeyAndUserId(projectKey, userId);
        projectMemberRepository.delete(member);
    }

    public List<ProjectMember> getMembers(String projectKey) {
        Project project = projectService.getByKey(projectKey);
        return projectMemberRepository.findByProject(project);
    }

    private ProjectMember findByProjectKeyAndUserId(String projectKey, Long userId) {
        Project project = projectService.getByKey(projectKey);
        User user = userService.findById(userId);
        return projectMemberRepository.findByProjectAndUser(project, user)
                .orElseThrow(() -> new NotFoundException("Project member not found"));
    }
}
