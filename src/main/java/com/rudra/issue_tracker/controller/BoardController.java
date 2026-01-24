package com.rudra.issue_tracker.controller;

import com.rudra.issue_tracker.controller.dto.*;
import com.rudra.issue_tracker.model.Issue;
import com.rudra.issue_tracker.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/project/{projectKey}")
    public ResponseEntity<BoardResponse> getProjectBoard(@PathVariable String projectKey) {
        log.info("GET /api/boards/project/{} called", projectKey);

        try {
            Map<String, List<Issue>> grouped = boardService.getProjectBoard(projectKey);

            log.debug("Project {} board grouped into {} columns",
                    projectKey, grouped.size());

            BoardResponse response =
                    toBoardResponse("PROJECT", projectKey, null, grouped);

            log.info("Project board response built successfully for {}", projectKey);
            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            log.error("Failed to fetch project board for projectKey={}", projectKey, ex);
            throw ex;
        }
    }

    @GetMapping("/sprint/{sprintId}")
    public ResponseEntity<BoardResponse> getSprintBoard(@PathVariable Long sprintId) {
        log.info("GET /api/boards/sprint/{} called", sprintId);

        try {
            Map<String, List<Issue>> grouped = boardService.getSprintBoard(sprintId);

            log.debug("Sprint {} board grouped into {} columns",
                    sprintId, grouped.size());

            BoardResponse response =
                    toBoardResponse("SPRINT", null, sprintId, grouped);

            log.info("Sprint board response built successfully for sprintId={}", sprintId);
            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            log.error("Failed to fetch sprint board for sprintId={}", sprintId, ex);
            throw ex;
        }
    }

    private BoardResponse toBoardResponse(
            String scope,
            String projectKey,
            Long sprintId,
            Map<String, List<Issue>> grouped) {

        log.debug("Building BoardResponse | scope={} projectKey={} sprintId={}",
                scope, projectKey, sprintId);

        List<BoardColumnResponse> columns = grouped.entrySet().stream()
                .map(entry -> {
                    log.trace("Mapping column status={} with {} issues",
                            entry.getKey(), entry.getValue().size());

                    return BoardColumnResponse.builder()
                            .status(entry.getKey())
                            .statusDisplayName(entry.getKey().replace('_', ' '))
                            .issues(entry.getValue().stream()
                                    .map(this::toIssueResponse)
                                    .toList())
                            .build();
                })
                .toList();

        return BoardResponse.builder()
                .scope(scope)
                .projectKey(projectKey)
                .sprintId(sprintId)
                .columns(columns)
                .build();
    }

    private IssueResponse toIssueResponse(Issue issue) {
        log.trace("Mapping Issue id={} key={}", issue.getId(), issue.getKey());

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
                .assigneeId(issue.getAssignee() != null
                        ? issue.getAssignee().getId()
                        : null)
                .dueDate(issue.getDueDate())
                .createdAt(issue.getCreatedAt())
                .updatedAt(issue.getUpdatedAt())
                .build();
    }
}
