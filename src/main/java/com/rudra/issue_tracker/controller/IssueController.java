    package com.rudra.issue_tracker.controller;

    import com.rudra.issue_tracker.controller.dto.*;
    import com.rudra.issue_tracker.exceptions.NotFoundException;
    import com.rudra.issue_tracker.model.*;
    import com.rudra.issue_tracker.service.IssueService;
    import com.rudra.issue_tracker.service.UserService;

    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.validation.annotation.Validated;
    import org.springframework.web.bind.annotation.*;

    import java.time.LocalDateTime;
    import java.time.LocalDate;
    import java.util.List;

    @RestController
    @RequestMapping("/api/issues")
    @RequiredArgsConstructor
    public class IssueController {

        private final IssueService issueService;
        private final UserService userService;

        @PostMapping
        public ResponseEntity<?> createIssue(@Validated @RequestBody CreateIssueRequest request) {
            try {
                User reporter = userService.findById(request.getReporterId());
                User assignee = request.getAssigneeId() != null
                        ? userService.findById(request.getAssigneeId())
                        : null;

                IssueType type = new IssueType();
                type.setId(request.getTypeId());

                IssuePriority priority = new IssuePriority();
                priority.setId(request.getPriorityId());

                Issue issue = issueService.createIssue(
                        request.getProjectKey(),
                        request.getSummary(),
                        request.getDescription(),
                        type,
                        priority,
                        reporter,
                        assignee
                );

                // Optional: parse due date after create, or include in service
                if (request.getDueDate() != null) {
                    LocalDate dd = LocalDate.parse(request.getDueDate());
                    issue.setDueDate(dd.atStartOfDay());
                }

                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(toResponse(issue));
            } catch (NotFoundException e) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(e.getMessage(),
                                HttpStatus.NOT_FOUND.value(),
                                LocalDateTime.now()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(e.getMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                LocalDateTime.now()));
            } catch (Exception e) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse("Failed to create issue: " + e.getMessage(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                LocalDateTime.now()));
            }
        }

        @GetMapping("/{key}")
        public ResponseEntity<?> getIssue(@PathVariable String key) {
            try {
                Issue issue = issueService.getByKey(key);
                return ResponseEntity.ok(toResponse(issue));
            } catch (NotFoundException e) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(e.getMessage(),
                                HttpStatus.NOT_FOUND.value(),
                                LocalDateTime.now()));
            }
        }

        @GetMapping("/project/{projectKey}")
        public ResponseEntity<List<IssueResponse>> getIssuesByProject(@PathVariable String projectKey) {
            List<IssueResponse> responses = issueService.getIssuesByProject(projectKey)
                    .stream()
                    .map(this::toResponse)
                    .toList();
            return ResponseEntity.ok(responses);
        }

        @PutMapping("/{key}")
        public ResponseEntity<?> updateIssue(@PathVariable String key,
                                             @RequestBody UpdateIssueRequest request) {
            try {
                LocalDateTime dueDate = null;
                if (request.getDueDate() != null) {
                    dueDate = LocalDate.parse(request.getDueDate()).atStartOfDay();
                }

                Issue issue = issueService.updateIssue(
                        key,
                        request.getSummary(),
                        request.getDescription(),
                        request.getPriorityId(),
                        request.getStatusId(),
                        request.getAssigneeId(),
                        dueDate
                );
                return ResponseEntity.ok(toResponse(issue));
            } catch (NotFoundException e) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(e.getMessage(),
                                HttpStatus.NOT_FOUND.value(),
                                LocalDateTime.now()));
            }
        }

        @DeleteMapping("/{key}")
        public ResponseEntity<?> deleteIssue(@PathVariable String key) {
            try {
                issueService.deleteIssue(key);
                return ResponseEntity.noContent().build();
            } catch (NotFoundException e) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(e.getMessage(),
                                HttpStatus.NOT_FOUND.value(),
                                LocalDateTime.now()));
            }
        }

        private IssueResponse toResponse(Issue issue) {
            return IssueResponse.builder()
                    .id(issue.getId())
                    .key(issue.getKey())
                    .projectKey(issue.getProject().getKey())
                    .summary(issue.getSummary())
                    .description(issue.getDescription())
                    .type(issue.getType().getName())
                    .priority(issue.getPriority().getName())
                    .status(issue.getStatus().getName())
                    .reporterId(issue.getReporter().getId())
                    .assigneeId(issue.getAssignee() != null ? issue.getAssignee().getId() : null)
                    .dueDate(issue.getDueDate())
                    .createdAt(issue.getCreatedAt())
                    .updatedAt(issue.getUpdatedAt())
                    .build();
        }
    }
