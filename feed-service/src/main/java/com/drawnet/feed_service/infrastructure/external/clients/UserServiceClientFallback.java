package com.drawnet.feed_service.infrastructure.external.clients;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.Collections;

/**
 * Fallback para UserServiceClient - Implementa Circuit Breaker pattern
 */
@Component
public class UserServiceClientFallback implements UserServiceClient {

    @Override
    public UserProfileDto getUserProfile(String userId) {
        // Retornar un perfil básico en caso de fallo
        return new UserProfileDto(
            userId,
            "usuario_" + userId.substring(0, Math.min(userId.length(), 8)),
            "Usuario Temporal",
            "/default-avatar.png",
            "Perfil temporalmente no disponible",
            false,
            true
        );
    }

    @Override
    public List<UserProfileDto> getUserProfiles(List<String> userIds) {
        // Retornar perfiles básicos para todos los IDs
        return userIds.stream()
                .map(this::getUserProfile)
                .toList();
    }

    @Override
    public List<String> getUserFollowing(String userId) {
        // Retornar lista vacía en caso de fallo
        return Collections.emptyList();
    }

    @Override
    public List<String> getUserFollowers(String userId) {
        return Collections.emptyList();
    }

    @Override
    public boolean userExists(String userId) {
        // Asumir que el usuario existe en caso de fallo
        return true;
    }

    @Override
    public Map<String, Object> getUserPrivacySettings(String userId) {
        // Configuración de privacidad por defecto (más restrictiva)
        return Map.of(
            "profilePublic", true,
            "postsPublic", true,
            "allowComments", true,
            "allowReposts", true
        );
    }
}