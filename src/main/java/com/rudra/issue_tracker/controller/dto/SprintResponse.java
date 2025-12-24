package com.rudra.issue_tracker.controller.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SprintResponse {
    private Long id;
    private String name;
    private String projectKey;
    private String status;
    private String goal;
    private LocalDate startDate;
    private LocalDate endDate;
}