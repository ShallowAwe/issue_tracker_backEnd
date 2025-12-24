package com.rudra.issue_tracker.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BoardColumnResponse {
    private String status;               // e.g. "TO_DO", "IN_PROGRESS", "DONE"
    private String statusDisplayName;    // e.g. "To Do"
    private List<IssueResponse> issues;  // issues in this column
}
