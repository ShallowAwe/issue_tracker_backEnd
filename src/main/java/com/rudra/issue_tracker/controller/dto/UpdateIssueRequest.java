// controller/dto/UpdateIssueRequest.java
package com.rudra.issue_tracker.controller.dto;

import lombok.Data;

@Data
public class UpdateIssueRequest {
    private String summary;
    private String description;
    private Integer priorityId;
    private Integer statusId;
    private Long assigneeId;
    private String dueDate;
}
