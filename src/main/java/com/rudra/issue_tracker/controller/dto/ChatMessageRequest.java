package com.rudra.issue_tracker.controller.dto;

import lombok.Data;

@Data
public class ChatMessageRequest {
    private Long receiverId;
    private String content;
}