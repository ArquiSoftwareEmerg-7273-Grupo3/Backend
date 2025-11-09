package com.drawnet.feed_service.application.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import java.util.HashMap;

/**
 * Servicio para enviar eventos WebSocket en tiempo real
 * 
 * Este servicio envía eventos a los clientes suscritos a los topics:
 * - /topic/post-created     → Nuevo post
 * - /topic/comment-created  → Nuevo comentario
 * - /topic/post-liked       → Like/unlike en post
 * - /topic/post-deleted     → Post eliminado
 * - /topic/post-updated     → Post actualizado
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Enviar evento cuando se crea un nuevo post
     * 
     * @param post El post creado (puede ser la entidad o un DTO)
     */
    public void sendPostCreatedEvent(Object post) {
        log.info("📝 [WebSocket] Enviando evento: post:created");
        messagingTemplate.convertAndSend("/topic/post-created", post);
    }

    /**
     * Enviar evento cuando se crea un nuevo comentario
     * 
     * @param postId ID del post al que pertenece el comentario
     * @param comment El comentario creado
     */
    public void sendCommentCreatedEvent(Long postId, Object comment) {
        log.info("💬 [WebSocket] Enviando evento: comment:created para post: {}", postId);
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("postId", postId);
        payload.put("comment", comment);
        
        messagingTemplate.convertAndSend("/topic/comment-created", payload);
    }

    /**
     * Enviar evento cuando se actualiza el contador de likes
     * 
     * @param postId ID del post
     * @param likesCount Nuevo contador de likes
     * @param userId ID del usuario que dio like/unlike
     */
    public void sendLikeUpdatedEvent(Long postId, int likesCount, Long userId) {
        log.info("❤️ [WebSocket] Enviando evento: post:liked para post: {} (likes: {})", postId, likesCount);
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("postId", postId);
        payload.put("likesCount", likesCount);
        payload.put("userId", userId);
        
        messagingTemplate.convertAndSend("/topic/post-liked", payload);
    }

    /**
     * Enviar evento cuando se elimina un post
     * 
     * @param postId ID del post eliminado
     * @param userId ID del usuario que eliminó el post
     */
    public void sendPostDeletedEvent(Long postId, Long userId) {
        log.info("🗑️ [WebSocket] Enviando evento: post:deleted: {}", postId);
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("postId", postId);
        payload.put("userId", userId);
        
        messagingTemplate.convertAndSend("/topic/post-deleted", payload);
    }

    /**
     * Enviar evento cuando se actualiza un post
     * 
     * @param post El post actualizado
     */
    public void sendPostUpdatedEvent(Object post) {
        log.info("✏️ [WebSocket] Enviando evento: post:updated");
        messagingTemplate.convertAndSend("/topic/post-updated", post);
    }
}
