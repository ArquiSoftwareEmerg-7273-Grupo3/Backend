package com.drawnet.artcollab.iam.application.internal.outboundservices.tokens;

public interface TokenService {
    String generateToken(String username);
    String generateToken(String username, Long userId);
    String generateToken(String username, Long userId, String role);
    String getUsernameFromToken(String token);
    boolean validateToken(String token);
}
