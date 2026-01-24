    package com.rudra.issue_tracker.controller;

    import com.rudra.issue_tracker.controller.dto.*;
    import com.rudra.issue_tracker.exceptions.NotFoundException;
    import com.rudra.issue_tracker.model.*;
    import com.rudra.issue_tracker.service.IssueService;
    import com.rudra.issue_tracker.service.ProjectService;
    import com.rudra.issue_tracker.service.UserService;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
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
    @Slf4j
    public class IssueController {

        private final IssueService issueService;
        private final UserService userService;
        private final ProjectService projectService;

        @PostMapping("createIssue")
        public ResponseEntity<?> createIssue(@Validated @RequestBody CreateIssueRequest request) {

            log.info(
                    "ISSUE | Create request | projectKey={} reporterId={} assigneeId={}",
                    request.getProjectKey(),
                    request.getReporterId(),
                    request.getAssigneeId()
            );

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
                        request.getDueDate(),
                        assignee  // Pass the User object, not the ID
                );

                log.info(
                        "ISSUE | Created | issueKey={} projectKey={} dueDate={}",
                        issue.getKey(),
                        issue.getProject().getKey(),
                        issue.getDueDate()
                );

                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(toResponse(issue));

            } catch (NotFoundException e) {
                log.warn(
                        "ISSUE | Create failed | NOT_FOUND | projectKey={} reason={}",
                        request.getProjectKey(),
                        e.getMessage()
                );

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(
                                e.getMessage(),
                                HttpStatus.NOT_FOUND.value(),
                                LocalDateTime.now()
                        ));

            } catch (IllegalArgumentException e) {
                log.warn(
                        "ISSUE | Create failed | BAD_REQUEST | reason={}",
                        e.getMessage()
                );

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(
                                e.getMessage(),
                                HttpStatus.BAD_REQUEST.value(),
                                LocalDateTime.now()
                        ));

            } catch (Exception e) {
                log.error(
                        "ISSUE | Create error | projectKey={}",
                        request.getProjectKey(),
                        e
                );

                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ErrorResponse(
                                "Failed to create issue",
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                LocalDateTime.now()
                        ));
            }
        }


        @GetMapping("/{key}")
        public ResponseEntity<?> getIssue(@PathVariable String key) {

            log.info("ISSUE | Fetch | issueKey={}", key);

            try {
                Issue issue = issueService.getByKey(key);

                log.info("ISSUE | Fetch success | issueKey={}", key);

                return ResponseEntity.ok(toResponse(issue));

            } catch (NotFoundException e) {
                log.warn("ISSUE | Fetch failed | NOT_FOUND | issueKey={}", key);

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(
                                e.getMessage(),
                                HttpStatus.NOT_FOUND.value(),
                                LocalDateTime.now()
                        ));
            }
        }


        @GetMapping("/project/{projectKey}")
        public ResponseEntity<List<IssueResponse>> getIssuesByProject(
                @PathVariable String projectKey) {

            log.info("ISSUE | Fetch by project | projectKey={}", projectKey);

            Project project = projectService.getByKey(projectKey);

            if (project == null) {
                log.warn("ISSUE | Project not found | projectKey={}", projectKey);
                throw new NotFoundException("Project not found with key: " + projectKey);
            }

            List<IssueResponse> responses = issueService
                    .getIssuesByProject(projectKey)
                    .stream()
                    .map(this::toResponse)
                    .toList();

            log.info(
                    "ISSUE | Fetch by project success | projectKey={} issueCount={}",
                    projectKey,
                    responses.size()
            );

            return ResponseEntity.ok(responses);
        }



        @PutMapping("/{key}")
        public ResponseEntity<?> updateIssue(
                @PathVariable String key,
                @RequestBody UpdateIssueRequest request) {

            log.info("ISSUE | Update request | issueKey={}", key);

            try {
                LocalDateTime dueDate = request.getDueDate() != null
                        ? LocalDate.parse(request.getDueDate()).atStartOfDay()
                        : null;

                Issue issue = issueService.updateIssue(
                        key,
                        request.getSummary(),
                        request.getDescription(),
                        request.getPriorityId(),
                        request.getStatusId(),
                        request.getAssigneeId(),
                        dueDate
                );

                log.info("ISSUE | Update success | issueKey={}", key);

                return ResponseEntity.ok(toResponse(issue));

            } catch (NotFoundException e) {
                log.warn("ISSUE | Update failed | NOT_FOUND | issueKey={}", key);

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(
                                e.getMessage(),
                                HttpStatus.NOT_FOUND.value(),
                                LocalDateTime.now()
                        ));
            }
        }

        @DeleteMapping("/{key}")
        public ResponseEntity<?> deleteIssue(@PathVariable String key) {

            log.info("ISSUE | Delete request | issueKey={}", key);

            try {
                issueService.deleteIssue(key);

                log.info("ISSUE | Delete success | issueKey={}", key);

                return ResponseEntity.noContent().build();

            } catch (NotFoundException e) {
                log.warn("ISSUE | Delete failed | NOT_FOUND | issueKey={}", key);

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse(
                                e.getMessage(),
                                HttpStatus.NOT_FOUND.value(),
                                LocalDateTime.now()
                        ));
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
