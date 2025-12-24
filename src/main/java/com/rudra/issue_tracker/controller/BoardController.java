package com.rudra.issue_tracker.controller;


import com.rudra.issue_tracker.controller.dto.*;
import com.rudra.issue_tracker.model.Issue;
import com.rudra.issue_tracker.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/project/{projectKey}")
    public ResponseEntity<BoardResponse> getProjectBoard(@PathVariable String projectKey) {
        Map<String, List<Issue>> grouped = boardService.getProjectBoard(projectKey);
        BoardResponse response = toBoardResponse("PROJECT", projectKey, null, grouped);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sprint/{sprintId}")
    public ResponseEntity<BoardResponse> getSprintBoard(@PathVariable Long sprintId) {
        Map<String, List<Issue>> grouped = boardService.getSprintBoard(sprintId);
        BoardResponse response = toBoardResponse("SPRINT", null, sprintId, grouped);
        return ResponseEntity.ok(response);
    }

    private BoardResponse toBoardResponse(String scope,
                                          String projectKey,
                                          Long sprintId,
                                          Map<String, List<Issue>> grouped) {
        List<BoardColumnResponse> columns = grouped.entrySet().stream()
                .map(entry -> BoardColumnResponse.builder()
                        .status(entry.getKey())
                        .statusDisplayName(entry.getKey().replace('_', ' '))
                        .issues(entry.getValue().stream().map(this::toIssueResponse).toList())
                        .build())
                .toList();

        return BoardResponse.builder()
                .scope(scope)
                .projectKey(projectKey)
                .sprintId(sprintId)
                .columns(columns)
                .build();
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

