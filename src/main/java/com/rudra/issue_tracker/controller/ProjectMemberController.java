package com.rudra.issue_tracker.controller;

import com.rudra.issue_tracker.controller.dto.AddProjectMemberRequest;
import com.rudra.issue_tracker.controller.dto.ProjectMemberResponse;
import com.rudra.issue_tracker.model.ProjectMember;
import com.rudra.issue_tracker.service.ProjectMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectKey}/members")
@RequiredArgsConstructor
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @PostMapping
    public ResponseEntity<ProjectMemberResponse> addMember(
            @PathVariable String projectKey,
            @RequestParam Long userId,
            @Valid @RequestBody AddProjectMemberRequest request,
            Authentication authentication) {

        // Only project OWNER can add members
        ProjectMember member = projectMemberService.addMember(
                projectKey, userId, request.getRoleName());

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(member));
    }

    @GetMapping
    public ResponseEntity<List<ProjectMemberResponse>> getMembers(@PathVariable String projectKey) {
        List<ProjectMemberResponse> members = projectMemberService.getMembers(projectKey)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(members);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ProjectMemberResponse> updateRole(
            @PathVariable String projectKey,
            @PathVariable Long userId,
            @Valid @RequestBody AddProjectMemberRequest request) {

        ProjectMember member = projectMemberService.updateRole(
                projectKey, userId, request.getRoleName());

        return ResponseEntity.ok(toResponse(member));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable String projectKey,
            @PathVariable Long userId) {

        projectMemberService.removeMember(projectKey, userId);
        return ResponseEntity.noContent().build();
    }

    private ProjectMemberResponse toResponse(ProjectMember member) {
        return ProjectMemberResponse.builder()
                .id(member.getId())
                .userId(member.getUser().getId())
                .username(member.getUser().getUsername())
                .roleName(member.getRole().getName())
                .roleDescription(member.getRole().getDescription())
                .build();
    }
}
