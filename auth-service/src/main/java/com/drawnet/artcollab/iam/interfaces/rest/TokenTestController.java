package com.drawnet.artcollab.iam.interfaces.rest;

import com.drawnet.artcollab.iam.application.internal.outboundservices.tokens.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/token-test")
@RequiredArgsConstructor
@Tag(name = "Token Test", description = "Endpoints para probar generación de tokens")
public class TokenTestController {

    private final TokenService tokenService;
    
    @Value("${authorization.jwt.secret}")
    private String secret;

    @PostMapping("/generate")
    @Operation(summary = "Generate test token with userId")
    public ResponseEntity<Map<String, Object>> generateTestToken(
            @RequestParam String username,
            @RequestParam Long userId) {
        
        String token = tokenService.generateToken(username, userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", username);
        response.put("userId", userId);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/decode")
    @Operation(summary = "Decode and verify token")
    public ResponseEntity<Map<String, Object>> decodeToken(
            @RequestParam String token) {
        
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("subject", claims.getSubject());
            response.put("userId", claims.get("userId"));
            response.put("issuedAt", claims.getIssuedAt());
            response.put("expiration", claims.getExpiration());
            response.put("allClaims", claims);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
