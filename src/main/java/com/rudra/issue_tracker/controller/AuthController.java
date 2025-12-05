package com.rudra.issue_tracker.controller;

import com.rudra.issue_tracker.controller.dto.LoginRequest;
import com.rudra.issue_tracker.controller.dto.RegisterRequest;
import com.rudra.issue_tracker.model.User;
import com.rudra.issue_tracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        User user = authService.register(
                request.getFullName(),
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        authService.authenticate(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok("Login successful");
    }
}
