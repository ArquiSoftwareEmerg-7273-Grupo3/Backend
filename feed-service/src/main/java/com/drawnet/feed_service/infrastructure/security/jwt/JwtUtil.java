package com.drawnet.feed_service.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class JwtUtil {

    @Value("${authorization.jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            log.info("Successfully extracted claims from token");
            return claims;
        } catch (Exception e) {
            log.error("Error extracting claims from token: {}", e.getMessage(), e);
            return null;
        }
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims != null ? claims.getSubject() : null;
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        if (claims != null) {
            log.debug("All claims in token: {}", claims);
            Object userIdClaim = claims.get("userId");
            if (userIdClaim != null) {
                log.debug("Found userId claim: {} (type: {})", userIdClaim, userIdClaim.getClass().getName());
                if (userIdClaim instanceof Number) {
                    return ((Number) userIdClaim).longValue();
                }
                return Long.valueOf(userIdClaim.toString());
            } else {
                log.warn("Token does not contain 'userId' claim. Available claims: {}", claims.keySet());
            }
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid token: {}", e.getMessage());
            return false;
        }
    }
}
