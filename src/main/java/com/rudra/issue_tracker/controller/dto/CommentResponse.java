package com.rudra.issue_tracker.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponse {
    private Long id;
    private String issueKey;
    private Long authorId;
    private String authorUsername;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
