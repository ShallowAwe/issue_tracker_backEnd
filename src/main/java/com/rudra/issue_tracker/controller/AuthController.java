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

        log.info("AUTH | Register attempt | username={} email={}",
                request.getUsername(), request.getEmail());

        try {
            User user = User.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .passwordHash(request.getPassword())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .build();

            User createdUser = userService.createUser(user);

            log.info("AUTH | Register success | userId={} username={}",
                    createdUser.getId(), createdUser.getUsername());

            String jwt = jwtTokenProvider.generateToken(
                    createdUser.getUsername(),
                    createdUser.getId()
            );

            return ResponseEntity.ok(new AuthResponse(jwt));

        } catch (IllegalStateException ex) {
            log.warn("AUTH | Register failed | reason={} username={}",
                    ex.getMessage(), request.getUsername());

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ex.getMessage());

        } catch (Exception ex) {
            log.error("AUTH | Register error | username={}",
                    request.getUsername(), ex);

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

        log.info("AUTH | Login attempt | username={}", request.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userService.findByUsername(request.getUsername());

            log.info("AUTH | Login success | userId={} username={}",
                    user.getId(), user.getUsername());

            String jwt = jwtTokenProvider.generateToken(
                    user.getUsername(),
                    user.getId()
            );

            return ResponseEntity.ok(new AuthResponse(jwt));

        } catch (BadCredentialsException ex) {
            log.warn("AUTH | Login failed | bad credentials | username={}",
                    request.getUsername());

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");

        } catch (Exception ex) {
            log.error("AUTH | Login error | username={}",
                    request.getUsername(), ex);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed due to server error");
        }
    }

    //Mapp this endpoint to a DTO  for response./././././.
    @GetMapping("/user-details")
    public ResponseEntity<UserDetailsResponse> getUserDetails(
            Authentication authentication
    ) {
        if (authentication == null) {
            log.warn("AUTH | User-details access without authentication");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();
        log.info("AUTH | Fetch user-details | username={}", username);

        UserDetailsResponse userDetails =
                userService.getUserDetailsByUsername(username);

        log.info("AUTH | User-details fetched | username={}", username);

        return ResponseEntity.ok(userDetails);
    }





}
