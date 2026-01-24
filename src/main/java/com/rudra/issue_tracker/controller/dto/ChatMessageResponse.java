package com.rudra.issue_tracker.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatMessageResponse {
    private Long conversationId;
    private Long messageId;
    private Long senderId;
    private String content;
    private Long sequenceNo;
    private LocalDateTime sentAt;
}