package com.rudra.issue_tracker.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Utility class for validating and extracting data from JWT tokens
    private final JwtTokenProvider jwtTokenProvider;

    // Service used to load user details from the database
    private final CustomUserDetailsService userDetailsService;

    /**
     * This method runs once for every incoming HTTP request.
     * It:
     * 1. Extracts the JWT token from the request header
     * 2. Validates the token
     * 3. Loads the user from the database
     * 4. Sets authentication in the Spring Security context
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Extract JWT token from Authorization header
        String token = getTokenFromRequest(request);

        // If token exists and is valid, authenticate the user
        if (token != null && jwtTokenProvider.validateToken(token)) {

            // Extract username from token
            String username = jwtTokenProvider.getUsernameFromToken(token);

            // Load user details from database
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Create authentication object with user authorities (roles/permissions)
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            // Attach request-specific authentication details (IP, session, etc.)
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            // Store the authentication in Spring Security context
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // Continue request processing through the remaining filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts JWT token from the Authorization header.
     * Expected format:
     * Authorization: Bearer <token>
     */
    private String getTokenFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        // Validate header format and remove "Bearer " prefix
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null; // No valid token found
    }
}
