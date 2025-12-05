package com.rudra.issue_tracker.service.impl;

import com.rudra.issue_tracker.model.User;
import com.rudra.issue_tracker.repository.UserRepository;
import com.rudra.issue_tracker.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public User register(String fullName, String email, String rawPassword) {
        if (userRepository.existsByEmail(email)){
            throw new RuntimeException("Email Already Exists");
        }
        User user = User.builder()
                .fullName(fullName)
                .email(email)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .isAdmin(false)
                .isActive(true)
                .build();
        return userRepository.save(user);
    }

    @Override
    public void authenticate(String email, String rawPassword) {
        Authentication authToken =
                new UsernamePasswordAuthenticationToken(email, rawPassword);
        authenticationManager.authenticate(authToken);
    }
}
