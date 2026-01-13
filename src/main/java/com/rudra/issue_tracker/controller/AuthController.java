package com.rudra.issue_tracker.controller;

import com.rudra.issue_tracker.controller.dto.*;
import com.rudra.issue_tracker.model.User;
import com.rudra.issue_tracker.security.JwtTokenProvider;
import com.rudra.issue_tracker.service.UserService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    // Handles authentication (login validation)
    private final AuthenticationManager authenticationManager;

    // Generates JWT tokens
    private final JwtTokenProvider jwtTokenProvider;

    // Handles all user-related database logic
    private final UserService userService;
    
    

    /**
     * ✅ USER REGISTRATION
     * - Validates duplicate username/email via UserService
     * - Encodes password inside service
     * - Returns JWT + userId on success
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        try {
            // Build user entity from request
            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .passwordHash(request.getPassword()) // Raw password → encoded in service
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .build();

            // Persist user using service
            User createdUser = userService.createUser(user);

            // Generate JWT token
            String jwt = jwtTokenProvider.generateToken(createdUser.getUsername());

            // Return token + user id
            return ResponseEntity.ok(
                    new AuthResponse(jwt, createdUser.getId())
            );

        } catch (IllegalStateException ex) {
            // Thrown when username or email already exists
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ex.getMessage());

        } catch (Exception ex) {
            // Any unexpected failure
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Registration failed due to server error");
        }
    }

    /**
     * ✅ USER LOGIN
     * - Authenticates credentials via AuthenticationManager
     * - Stores authentication in SecurityContext
     * - Returns JWT + userId
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginRequest request) {

        try {
            // Authenticate username + password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Store authentication in security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String jwt = jwtTokenProvider.generateToken(authentication);

            // Fetch user from database
            User user = userService.findByUsername(request.getUsername());

            // Return token + user id
            return ResponseEntity.ok(
                    new AuthResponse(jwt, user.getId())
            );

        } catch (BadCredentialsException ex) {
            // Invalid login credentials
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");

        } catch (Exception ex) {

            // Any unexpected failure
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed due to server error");
        }
    }

    @GetMapping("/user-details")
    public ResponseEntity<User> getUserDetails(Authentication authentication) {

        String username = authentication.getName();

        User userDetails = userService.findByUsername(username);

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(userDetails);
    }




}
