package com.rudra.issue_tracker.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsResponse {

    private long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private boolean active;
}
