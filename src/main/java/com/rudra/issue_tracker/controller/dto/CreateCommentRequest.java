package com.rudra.issue_tracker.controller.dto;


import lombok.Data;

@Data
public class CreateCommentRequest {

    private String issueKey;
    private Long authorId;
    private String body;
}