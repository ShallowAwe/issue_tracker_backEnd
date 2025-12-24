package com.rudra.issue_tracker.controller.dto;

import lombok.*;


@Getter
@Setter
public class RegisterRequest {

 private String username;
 private String email;
 private String password;
    private String firstName;
    private String lastName;

}
