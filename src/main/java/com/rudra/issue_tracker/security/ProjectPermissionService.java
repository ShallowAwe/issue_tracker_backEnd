// ProjectPermissionService.java
package com.rudra.issue_tracker.security;

import com.rudra.issue_tracker.exceptions.NotFoundException;
import com.rudra.issue_tracker.model.Project;
import com.rudra.issue_tracker.model.ProjectMember;
import com.rudra.issue_tracker.model.User;
import com.rudra.issue_tracker.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectPermissionService {

    private final ProjectMemberRepository projectMemberRepository;

    public boolean canManageProject(Project project, User user) {
        ProjectMember pm = getMembership(project, user);
        String role = pm.getRole().getName();
        // OWNER can do everything, DEVELOPER can edit issues, VIEWER read-only[web:117]
        return "OWNER".equals(role);
    }

    public boolean canEditIssues(Project project, User user) {
        ProjectMember pm = getMembership(project, user);
        String role = pm.getRole().getName();
        return "OWNER".equals(role) || "DEVELOPER".equals(role);
    }

    public boolean canViewProject(Project project, User user) {
        // any membership can view
        getMembership(project, user);
        return true;
    }

    private ProjectMember getMembership(Project project, User user) {
        return projectMemberRepository.findByProjectAndUser(project, user)
                .orElseThrow(() -> new NotFoundException("User not in project"));
    }
}
