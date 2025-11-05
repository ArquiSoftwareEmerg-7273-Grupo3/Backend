package com.drawnet.feed_service.infrastructure.external.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Cliente Feign para comunicación con el servicio de usuarios
 */
@FeignClient(
    name = "auth-service", 
    path = "/api/v1/users",
    fallback = UserServiceClientFallback.class
)
public interface UserServiceClient {

    /**
     * Obtener información básica de un usuario
     */
    @GetMapping("/{userId}/profile")
    UserProfileDto getUserProfile(@PathVariable("userId") String userId);

    /**
     * Obtener múltiples perfiles de usuarios
     */
    @GetMapping("/profiles")
    List<UserProfileDto> getUserProfiles(@RequestParam("userIds") List<String> userIds);

    /**
     * Obtener usuarios que sigue un usuario específico
     */
    @GetMapping("/{userId}/following")
    List<String> getUserFollowing(@PathVariable("userId") String userId);

    /**
     * Obtener seguidores de un usuario
     */
    @GetMapping("/{userId}/followers")
    List<String> getUserFollowers(@PathVariable("userId") String userId);

    /**
     * Verificar si un usuario existe
     */
    @GetMapping("/{userId}/exists")
    boolean userExists(@PathVariable("userId") String userId);

    /**
     * Obtener configuraciones de privacidad del usuario
     */
    @GetMapping("/{userId}/privacy")
    Map<String, Object> getUserPrivacySettings(@PathVariable("userId") String userId);

    /**
     * DTO para información básica del usuario
     */
    record UserProfileDto(
        String id,
        String username,
        String displayName,
        String profilePicture,
        String bio,
        boolean verified,
        boolean active
    ) {}
}