package com.rudra.issue_tracker.service;

import com.rudra.issue_tracker.controller.dto.UserDetailsResponse;
import com.rudra.issue_tracker.exceptions.NotFoundException;
import com.rudra.issue_tracker.model.User;
import com.rudra.issue_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    // JPA repository for database operations on User entity
    private final UserRepository userRepository;

    // Password encoder used to securely hash raw passwords
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates and persists a new user.
     * Steps:
     * 1. Validate unique username
     * 2. Validate unique email
     * 3. Encrypt the raw password
     * 4. Save user to database
     */
    @Transactional
    public User createUser(User user) {

        // Prevent duplicate usernames
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalStateException("Username already exists");
        }

        // Prevent duplicate emails
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalStateException("Email already exists");
        }

        // Encrypt the raw password before storing
        user.setPasswordHash(
                passwordEncoder.encode(user.getPasswordHash()));
        user.setIsActive(true);

        // Persist and return the saved user
        return userRepository.save(user);
    }

    /**
     * Fetches a user by database ID.
     * Throws NotFoundException if user does not exist.
     */
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    /**
     * Fetches a user using username.
     * Used heavily during authentication & authorization.
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found: " + username));
    }

    public UserDetailsResponse getUserDetailsByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDetailsResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getIsActive());
    }

}
