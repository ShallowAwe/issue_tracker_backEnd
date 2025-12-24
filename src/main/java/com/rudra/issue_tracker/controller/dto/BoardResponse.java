package com.rudra.issue_tracker.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoardResponse {
    private String scope;        // "PROJECT" or "SPRINT"
    private String projectKey;
    private Long sprintId;       // null for backlog board
    private List<BoardColumnResponse> columns;
}