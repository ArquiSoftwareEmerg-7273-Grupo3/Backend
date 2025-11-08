package com.drawnet.feed_service.application.services;

import com.drawnet.feed_service.infrastructure.external.clients.UserServiceClient;
import com.drawnet.feed_service.infrastructure.external.clients.PortfolioServiceClient;
import com.drawnet.feed_service.infrastructure.external.clients.ChatServiceClient;
import com.drawnet.feed_service.domain.model.aggregates.Post;
import com.drawnet.feed_service.domain.model.entities.Comment;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio para integrar información de otros microservicios
 */
@Service
public class MicroserviceIntegrationService {

    private final UserServiceClient userServiceClient;
    private final PortfolioServiceClient portfolioServiceClient;
    private final ChatServiceClient chatServiceClient;

    public MicroserviceIntegrationService(
            UserServiceClient userServiceClient,
            PortfolioServiceClient portfolioServiceClient,
            ChatServiceClient chatServiceClient) {
        this.userServiceClient = userServiceClient;
        this.portfolioServiceClient = portfolioServiceClient;
        this.chatServiceClient = chatServiceClient;
    }

    /**
     * Enriquecer post con información de usuario
     */
    @Cacheable(value = "userProfiles", key = "#userId")
    public UserServiceClient.UserProfileDto getUserProfile(String userId) {
        try {
            return userServiceClient.getUserProfile(userId);
        } catch (Exception e) {
            // Fallback ya manejado por Feign
            return userServiceClient.getUserProfile(userId);
        }
    }

    /**
     * Obtener información de múltiples usuarios para el feed
     */
    public Map<String, UserServiceClient.UserProfileDto> getUserProfiles(List<String> userIds) {
        try {
            List<UserServiceClient.UserProfileDto> profiles = 
                userServiceClient.getUserProfiles(userIds);
            
            return profiles.stream()
                    .collect(Collectors.toMap(
                        UserServiceClient.UserProfileDto::id,
                        profile -> profile
                    ));
        } catch (Exception e) {
            // En caso de error, obtener perfiles individuales
            return userIds.stream()
                    .collect(Collectors.toMap(
                        userId -> userId,
                        this::getUserProfile
                    ));
        }
    }

    /**
     * Obtener usuarios que sigue un usuario para generar feed personalizado
     */
    public List<String> getUserFollowing(String userId) {
        try {
            return userServiceClient.getUserFollowing(userId);
        } catch (Exception e) {
            return List.of(); // Lista vacía si falla
        }
    }

    /**
     * Verificar si un usuario puede ver/interactuar con un post
     */
    public boolean canUserInteractWithPost(String userId, String postOwnerId) {
        try {
            Map<String, Object> privacySettings = 
                userServiceClient.getUserPrivacySettings(postOwnerId);
            
            Boolean postsPublic = (Boolean) privacySettings.get("postsPublic");
            Boolean allowComments = (Boolean) privacySettings.get("allowComments");
            
            return Boolean.TRUE.equals(postsPublic) && Boolean.TRUE.equals(allowComments);
        } catch (Exception e) {
            return true; // Por defecto permitir interacción
        }
    }

    /**
     * Obtener información de obra de arte para posts relacionados
     */
    public PortfolioServiceClient.ArtworkDto getArtworkInfo(String artworkId) {
        try {
            return portfolioServiceClient.getArtwork(artworkId);
        } catch (Exception e) {
            return portfolioServiceClient.getArtwork(artworkId); // Fallback
        }
    }

    /**
     * Obtener estadísticas del portafolio para recomendaciones
     */
    public PortfolioServiceClient.PortfolioStatsDto getPortfolioStats(String userId) {
        try {
            return portfolioServiceClient.getPortfolioStats(userId);
        } catch (Exception e) {
            return portfolioServiceClient.getPortfolioStats(userId); // Fallback
        }
    }

    /**
     * Enviar notificación cuando alguien interactúa con un post
     */
    public void notifyPostInteraction(Post post, String actorUserId, 
                                     ChatServiceClient.NotificationType type) {
        if (post.getAuthorId().toString().equals(actorUserId)) {
            return; // No notificar interacciones propias
        }

        try {
            ChatServiceClient.FeedInteractionNotificationDto notification = 
                new ChatServiceClient.FeedInteractionNotificationDto(
                    post.getAuthorId().toString(), // recipient
                    actorUserId,      // actor
                    post.getId().toString(),
                    type,
                    generateNotificationMessage(type, actorUserId),
                    LocalDateTime.now()
                );

            chatServiceClient.sendFeedInteractionNotification(notification);
        } catch (Exception e) {
            // Las notificaciones no son críticas, solo logear
            System.out.println("Failed to send notification: " + e.getMessage());
        }
    }

    /**
     * Enviar notificación para respuesta a comentario
     */
    public void notifyCommentReply(Comment originalComment, Comment reply) {
        if (originalComment.getUserId().equals(reply.getUserId())) {
            return; // No notificar respuestas propias
        }

        try {
            ChatServiceClient.FeedInteractionNotificationDto notification = 
                new ChatServiceClient.FeedInteractionNotificationDto(
                    originalComment.getUserId().toString(),
                    reply.getUserId().toString(),
                    originalComment.getPost().getId().toString(),
                    ChatServiceClient.NotificationType.COMMENT_REPLY,
                    "Ha respondido a tu comentario",
                    LocalDateTime.now()
                );

            chatServiceClient.sendFeedInteractionNotification(notification);
        } catch (Exception e) {
            System.out.println("Failed to send comment reply notification: " + e.getMessage());
        }
    }

    /**
     * Verificar si un usuario está online para optimizar notificaciones
     */
    public boolean isUserOnline(String userId) {
        try {
            return chatServiceClient.isUserOnline(userId);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Enviar notificación push para interacciones importantes
     */
    public void sendPushNotificationForHighEngagement(Post post, String actorUserId, 
                                                     ChatServiceClient.NotificationType type) {
        try {
            // Solo enviar push si el post tiene mucho engagement o es reciente
            boolean shouldSendPush = post.getCreatedAt().isAfter(LocalDateTime.now().minusHours(24)) ||
                                   (post.getReactionsCount() + post.getCommentsCount()) > 50;

            if (shouldSendPush) {
                ChatServiceClient.PushNotificationDto pushNotification = 
                    new ChatServiceClient.PushNotificationDto(
                        post.getAuthorId().toString(),
                        "Nueva interacción en tu post",
                        generateNotificationMessage(type, actorUserId),
                        "OPEN_POST",
                        post.getId().toString()
                    );

                chatServiceClient.sendPushNotification(pushNotification);
            }
        } catch (Exception e) {
            System.out.println("Failed to send push notification: " + e.getMessage());
        }
    }

    /**
     * Obtener categorías trending para recomendaciones de contenido
     */
    public List<String> getTrendingCategories() {
        try {
            return portfolioServiceClient.getTrendingCategories();
        } catch (Exception e) {
            return List.of("arte", "digital", "tradicional", "diseño");
        }
    }

    // Métodos auxiliares privados

    private String generateNotificationMessage(ChatServiceClient.NotificationType type, String actorUserId) {
        return switch (type) {
            case POST_LIKE -> "le ha gustado tu post";
            case POST_COMMENT -> "ha comentado en tu post";
            case POST_REPOST -> "ha compartido tu post";
            case COMMENT_LIKE -> "le ha gustado tu comentario";
            case COMMENT_REPLY -> "ha respondido a tu comentario";
            case FOLLOW -> "ha comenzado a seguirte";
        };
    }
}