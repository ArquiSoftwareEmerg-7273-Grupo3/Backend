package com.drawnet.feed_service.interfaces.rest;

import com.drawnet.feed_service.infrastructure.security.jwt.CurrentUserId;
import com.drawnet.feed_service.infrastructure.security.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth-test")
@RequiredArgsConstructor
@Tag(name = "Auth Test", description = "Endpoints para probar la autenticación JWT")
public class AuthTestController {

    private final JwtUtil jwtUtil;

    @GetMapping("/verify-token")
    @Operation(summary = "Verify JWT token and extract claims")
    public ResponseEntity<Map<String, Object>> verifyToken(
            @RequestHeader("Authorization") String authHeader) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            try {
                var claims = jwtUtil.extractAllClaims(token);
                String username = jwtUtil.extractUsername(token);
                Long userId = jwtUtil.extractUserId(token);
                boolean isValid = jwtUtil.validateToken(token);
                
                response.put("valid", isValid);
                response.put("username", username);
                response.put("userId", userId);
                response.put("allClaims", claims);
                
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                response.put("error", e.getMessage());
                return ResponseEntity.badRequest().body(response);
            }
        }
        
        response.put("error", "No Authorization header found");
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/test-current-user")
    @Operation(summary = "Test @CurrentUserId annotation")
    public ResponseEntity<Map<String, Object>> testCurrentUser(
            @Parameter(hidden = true) @CurrentUserId Long userId) {
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("message", "Successfully extracted userId from token!");
        
        return ResponseEntity.ok(response);
    }
}
