package com.rudra.issue_tracker.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    // Base64-encoded secret key for signing JWT tokens
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    // Token expiration time in milliseconds (default = 24 hours)
    @Value("${app.jwt-expiration:86400000}")
    private long jwtExpirationMs;

    /**
     * Converts the Base64-encoded secret into a secure HMAC SHA-256 key.
     * This key is used both for signing and verifying JWT tokens.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT token using Spring Security Authentication object.
     */
    public String generateToken(Authentication authentication) {
        return generateToken(authentication.getName());
    }

    /**
     * Generates a JWT token using a username as the subject.
     * The token contains:
     * - subject (username)
     * - issued timestamp
     * - expiration timestamp
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)          // Sets the token subject (user identifier)
                .setIssuedAt(now)              // Token creation time
                .setExpiration(expiryDate)    // Token expiry time
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Secure signature
                .compact();                    // Builds the token
    }

    /**
     * Extracts the username (subject) from a valid JWT token.
     * Allows small clock drift (60 seconds) between systems.
     */
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setAllowedClockSkewSeconds(60)  // Tolerates small time mismatch between servers
                .setSigningKey(getSigningKey())  // Validates token signature
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // Returns the stored username
    }

    /**
     * Validates the JWT token by checking:
     * - Signature
     * - Expiration
     * - Structure
     * - Unsupported claims
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setAllowedClockSkewSeconds(60)
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);

            return true; // Token is valid

        } catch (ExpiredJwtException ex) {
            log.warn("JWT expired: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.warn("JWT unsupported: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.warn("JWT malformed: {}", ex.getMessage());
        } catch (SecurityException ex) {
            log.warn("JWT signature invalid: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.warn("JWT is empty: {}", ex.getMessage());
        }

        return false; // Token is invalid
    }
}
