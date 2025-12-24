package com.rudra.issue_tracker.controller;

import com.rudra.issue_tracker.controller.dto.CreateSprintRequest;
import com.rudra.issue_tracker.controller.dto.ErrorResponse;
import com.rudra.issue_tracker.controller.dto.IssueResponse;
import com.rudra.issue_tracker.controller.dto.SprintResponse;
import com.rudra.issue_tracker.exceptions.NotFoundException;
import com.rudra.issue_tracker.model.Issue;
import com.rudra.issue_tracker.model.Sprint;
import com.rudra.issue_tracker.service.IssueService;
import com.rudra.issue_tracker.service.SprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sprints")
@RequiredArgsConstructor
public class SprintController {

    private final SprintService sprintService;
    private  final IssueService issueService;

    @PostMapping
    public ResponseEntity<?> createSprint(@Validated @RequestBody CreateSprintRequest request) {
        try {
            LocalDate start = request.getStartDate() != null ? LocalDate.parse(request.getStartDate()) : null;
            LocalDate end = request.getEndDate() != null ? LocalDate.parse(request.getEndDate()) : null;

            Sprint sprint = sprintService.createSprint(
                    request.getProjectKey(),
                    request.getName(),
                    start,
                    end,
                    request.getGoal()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(sprint));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage(), 400, LocalDateTime.now()));
        }
    }

    @GetMapping("/project/{projectKey}")
    public ResponseEntity<List<SprintResponse>> getProjectSprints(@PathVariable String projectKey) {
        List<SprintResponse> list = sprintService.getSprintsByProject(projectKey)
                .stream().map(this::toResponse).toList();
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(@PathVariable Long id, @RequestParam String status) {
        Sprint sprint = sprintService.changeStatus(id, status);
        return ResponseEntity.ok(toResponse(sprint));
    }

    private SprintResponse toResponse(Sprint s) {
        return SprintResponse.builder()
                .id(s.getId())
                .name(s.getName())
                .projectKey(s.getProject().getKey())
                .status(s.getStatus())
                .goal(s.getGoal())
                .startDate(s.getStartDate())
                .endDate(s.getEndDate())
                .build();
    }


    @GetMapping("/{id}/issues")
    public ResponseEntity<List<IssueResponse>> getSprintIssues(@PathVariable Long id) {
        List<IssueResponse> list = issueService.getIssuesBySprint(id)
                .stream()
                .map(this::toIssueResponse)
                .toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{id}/issues/{issueKey}")
    public ResponseEntity<?> addIssueToSprint(@PathVariable Long id,
                                              @PathVariable String issueKey) {
        try {
            Issue issue = issueService.assignIssueToSprint(issueKey, id);
            return ResponseEntity.ok(toIssueResponse(issue));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage(),
                            HttpStatus.NOT_FOUND.value(),
                            LocalDateTime.now()));
        }
    }

    @DeleteMapping("/{id}/issues/{issueKey}")
    public ResponseEntity<?> removeIssueFromSprint(@PathVariable Long id,
                                                   @PathVariable String issueKey) {
        try {
            Issue issue = issueService.removeIssueFromSprint(issueKey);
            return ResponseEntity.ok(toIssueResponse(issue));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(e.getMessage(),
                            HttpStatus.NOT_FOUND.value(),
                            LocalDateTime.now()));
        }
    }

    private IssueResponse toIssueResponse(Issue issue) {
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