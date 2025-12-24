package com.rudra.issue_tracker.controller.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSprintRequest {

    private String projectKey;

    private String name;
    private String startDate; // "2025-01-01"
    private String endDate;
    private String goal;
}
