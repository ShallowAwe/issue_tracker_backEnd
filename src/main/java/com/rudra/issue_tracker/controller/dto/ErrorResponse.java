package com.rudra.issue_tracker.controller.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ErrorResponse {

    private String message;
    private int status;
    private LocalDateTime timestamp;
}
