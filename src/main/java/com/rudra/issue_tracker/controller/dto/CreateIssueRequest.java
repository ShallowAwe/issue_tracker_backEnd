// controller/dto/CreateIssueRequest.java
package com.rudra.issue_tracker.controller.dto;


import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateIssueRequest {
    private String projectKey;     // e.g. "PROJ"


    private String summary;        // Jira "Summary"[web:54]

    private String description;    // Jira "Description"[web:54]


    private Integer typeId;        // BUG, TASK, STORY etc.


    private Integer priorityId;    // LOW, MEDIUM, HIGH etc.

    private Long reporterId;

    private Long assigneeId;

    private String dueDate;        // ISO string, e.g. "2025-12-31"
}
