package com.rudra.issue_tracker.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectMemberResponse {

    private Long id;
    private Long userId;
    private String username;
    private String roleName;
    private String roleDescription;

}