package com.rudra.issue_tracker.controller;

import com.rudra.issue_tracker.controller.dto.CreateProjectRequest;
import com.rudra.issue_tracker.controller.dto.UpdateProjectRequest;
import com.rudra.issue_tracker.controller.dto.ProjectResponse;
import com.rudra.issue_tracker.controller.dto.ErrorResponse;
import com.rudra.issue_tracker.exceptions.NotFoundException;
import com.rudra.issue_tracker.model.Project;
import com.rudra.issue_tracker.model.User;
import com.rudra.issue_tracker.service.ProjectMemberService;
import com.rudra.issue_tracker.service.ProjectService;
import com.rudra.issue_tracker.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMemberService projectMemberService;
    private final UserService userService;

    /**
     * ✅ CREATE PROJECT
     * Requires authenticated user.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createProject(@Validated @RequestBody CreateProjectRequest request,
                                           Authentication authentication) {
        try {
            // Fetch currently authenticated user
            User owner = userService.findByUsername(authentication.getName());

            // Create project
            Project project = projectService.createProject(
                    request.getKey(),
                    request.getName(),
                    request.getDescription(),
                    owner
            );
            projectMemberService.addMember(project, owner, "OWNER");
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(toResponse(project));

        } catch (IllegalArgumentException e) {
            // Duplicate key or validation failure
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(
                            e.getMessage(),
                            HttpStatus.BAD_REQUEST.value(),
                            LocalDateTime.now()
                    ));

        } catch (NotFoundException e) {
            // Authenticated user not found in DB
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(
                            e.getMessage(),
                            HttpStatus.NOT_FOUND.value(),
                            LocalDateTime.now()
                    ));

        } catch (Exception e) {
            // Unexpected server failure
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(
                            "Failed to create project",
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            LocalDateTime.now()
                    ));
        }
    }

    /**
     * ✅ GET ALL PROJECTS
     */
    @GetMapping("/getProjects")
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {

        List<ProjectResponse> responses = projectService.getAll()
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    /**
     * ✅ GET PROJECT BY KEY
     */
    @GetMapping("/{key}/get")
    public ResponseEntity<?> getProjectByKey(@PathVariable String key) {
        try {
            Project project = projectService.getByKey(key);
            return ResponseEntity.ok(toResponse(project));

        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(
                            e.getMessage(),
                            HttpStatus.NOT_FOUND.value(),
                            LocalDateTime.now()
                    ));
        }
    }

    /**
     * ✅ UPDATE PROJECT
     */
    @PutMapping("/{key}/update")
    public ResponseEntity<?> updateProject(@PathVariable String key,
                                           @RequestBody UpdateProjectRequest request) {
        try {
            Project project = projectService.updateProject(
                    key,
                    request.getName(),
                    request.getDescription(),
                    request.getArchived()
            );

            return ResponseEntity.ok(toResponse(project));

        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(
                            e.getMessage(),
                            HttpStatus.NOT_FOUND.value(),
                            LocalDateTime.now()
                    ));
        }
    }

    /**
     * ✅ ARCHIVE PROJECT
     */
    @PatchMapping("/{key}/archive")
    public ResponseEntity<?> archiveProject(@PathVariable String key) {
        try {
            Project project = projectService.updateProject(key, null, null, true);
            return ResponseEntity.ok(toResponse(project));

        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(
                            e.getMessage(),
                            HttpStatus.NOT_FOUND.value(),
                            LocalDateTime.now()
                    ));
        }
    }

    /**
     * ✅ DELETE PROJECT
     */
    @DeleteMapping("/{key}/delete")
    public ResponseEntity<?> deleteProject(@PathVariable String key) {
        try {
            projectService.deleteProject(key);
            return ResponseEntity.noContent().build();

        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(
                            e.getMessage(),
                            HttpStatus.NOT_FOUND.value(),
                            LocalDateTime.now()
                    ));
        }
    }

    /**
     * ✅ ENTITY → API RESPONSE MAPPER
     */
    private ProjectResponse toResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .key(project.getKey())
                .name(project.getName())
                .description(project.getDescription())
                .ownerId(project.getOwner().getId())
                .archived(project.getArchived())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
