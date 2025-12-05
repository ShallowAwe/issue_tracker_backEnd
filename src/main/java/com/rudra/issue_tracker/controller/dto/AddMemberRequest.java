package com.rudra.issue_tracker.controller.dto;


import com.rudra.issue_tracker.model.TeamRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberRequest {

    private Long userId;
    private TeamRole role;
    private  Long actionByUserId;
}
