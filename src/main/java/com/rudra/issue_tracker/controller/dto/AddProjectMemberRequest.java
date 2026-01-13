package com.rudra.issue_tracker.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddProjectMemberRequest {
    @NotBlank
    private String roleName; // OWNER, DEVELOPER, TESTER, VIEWER
}
