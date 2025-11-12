package com.drawnet.artcollab.portafolioservice.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * Servicio para decodificar y validar tokens JWT.
 * Extrae el userId (subject) del token para identificar al usuario autenticado.
 */
@Service
public class JwtService {

    @Value("${jwt.secret:WriteHereYourSecretStringForTokenSigningCredentials}")
    private String secretKey;

    /**
     * Extrae el userId del token JWT desde el claim "userId".
     * IMPORTANTE: El userId corresponde al ID del User (tabla users), 
     * NO al ilustradorId (tabla ilustrador).
     * Para obtener el ilustradorId, se debe hacer una consulta adicional 
     * al auth-service usando este userId.
     * 
     * @param token El token JWT (sin el prefijo "Bearer ")
     * @return El userId del usuario autenticado
     * @throws io.jsonwebtoken.JwtException si el token es inválido
     */
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        // El userId está en los claims, no en el subject
        Object userIdObj = claims.get("userId");
        if (userIdObj == null) {
            throw new IllegalArgumentException("Token no contiene el claim 'userId'");
        }
        if (userIdObj instanceof Number) {
            return ((Number) userIdObj).longValue();
        }
        return Long.parseLong(userIdObj.toString());
    }

    /**
     * Extrae el rol del usuario del token JWT.
     * @param token El token JWT
     * @return El rol del usuario (ILUSTRADOR, CONSUMIDOR, etc.)
     */
    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }

    /**
     * Valida si el token pertenece a un ilustrador.
     * @param token El token JWT
     * @return true si el usuario tiene rol ILUSTRADOR
     */
    public boolean isIlustrador(String token) {
        String role = extractRole(token);
        return "ILUSTRADOR".equals(role);
    }

    /**
     * Extrae todos los claims del token.
     */
    private Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Limpia el token removiendo el prefijo "Bearer " si existe.
     * @param authHeader El header Authorization completo
     * @return El token limpio
     */
    public String cleanToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return authHeader;
    }
}
