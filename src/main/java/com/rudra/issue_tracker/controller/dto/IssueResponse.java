// controller/dto/IssueResponse.java
package com.rudra.issue_tracker.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class IssueResponse {
    private Long id;
    private String key;
    private String projectKey;
    private String summary;
    private String description;
    private String type;
    private String priority;
    private String status;
    private Long reporterId;
    private Long assigneeId;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
