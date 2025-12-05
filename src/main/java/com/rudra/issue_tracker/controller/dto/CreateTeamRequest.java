package com.rudra.issue_tracker.controller.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTeamRequest {

    private  String name;
    private String description;
    private Long creatorUserId;
}
