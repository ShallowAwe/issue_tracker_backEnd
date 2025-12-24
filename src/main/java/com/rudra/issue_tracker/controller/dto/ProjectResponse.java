// controller/dto/ProjectResponse.java
package com.rudra.issue_tracker.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProjectResponse {
    private Long id;
    private String key;
    private String name;
    private String description;
    private Long ownerId;
    private Boolean archived;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

