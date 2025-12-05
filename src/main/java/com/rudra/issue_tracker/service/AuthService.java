package com.rudra.issue_tracker.service;

import com.rudra.issue_tracker.model.User;

public interface AuthService {

    User register(String fullName,String email, String rawPassword);
    void authenticate(String email, String rawPassword);
}
