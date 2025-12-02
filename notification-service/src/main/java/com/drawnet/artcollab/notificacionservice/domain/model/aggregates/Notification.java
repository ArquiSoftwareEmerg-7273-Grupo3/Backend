package com.drawnet.artcollab.notificacionservice.domain.model.aggregates;

import com.drawnet.artcollab.notificacionservice.domain.model.valueobjects.NotificationPriority;
import com.drawnet.artcollab.notificacionservice.domain.model.valueobjects.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Agregado raíz para las notificaciones
 * Representa una notificación enviada a un usuario
 */
@Entity
@Table(name = "notifications")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * ID del usuario receptor de la notificación
     */
    @Column(name = "recipient_user_id", nullable = false)
    private Long recipientUserId;
    
    /**
     * ID del usuario que generó la acción (puede ser null para notificaciones del sistema)
     */
    @Column(name = "actor_user_id")
    private Long actorUserId;
    
    /**
     * Tipo de notificación
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NotificationType type;
    
    /**
     * Título de la notificación
     */
    @Column(nullable = false, length = 255)
    private String title;
    
    /**
     * Mensaje de la notificación
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    
    /**
     * Prioridad de la notificación
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationPriority priority = NotificationPriority.NORMAL;
    
    /**
     * Indica si la notificación ha sido leída
     */
    @Column(nullable = false)
    private Boolean isRead = false;
    
    /**
     * Fecha y hora en que se leyó la notificación
     */
    @Column(name = "read_at")
    private LocalDateTime readAt;
    
    /**
     * ID de la entidad relacionada (post, proyecto, comentario, etc.)
     */
    @Column(name = "related_entity_id")
    private Long relatedEntityId;
    
    /**
     * Tipo de entidad relacionada (POST, PROJECT, COMMENT, etc.)
     */
    @Column(name = "related_entity_type", length = 50)
    private String relatedEntityType;
    
    /**
     * URL de acción (para navegar al contenido relacionado)
     */
    @Column(name = "action_url", length = 500)
    private String actionUrl;
    
    /**
     * Datos adicionales en formato JSON (opcional)
     */
    @Column(columnDefinition = "TEXT")
    private String metadata;
    
    /**
     * Fecha de creación
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * Fecha de expiración (opcional, para notificaciones temporales)
     */
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    /**
     * Indica si la notificación está activa
     */
    @Column(nullable = false)
    private Boolean active = true;
    
    // Constructores
    public Notification() {
    }
    
    public Notification(Long recipientUserId, Long actorUserId, NotificationType type, 
                       String title, String message) {
        this.recipientUserId = recipientUserId;
        this.actorUserId = actorUserId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.priority = NotificationPriority.NORMAL;
        this.isRead = false;
        this.active = true;
    }
    
    // Métodos de negocio
    
    /**
     * Marca la notificación como leída
     */
    public void markAsRead() {
        if (!this.isRead) {
            this.isRead = true;
            this.readAt = LocalDateTime.now();
        }
    }
    
    /**
     * Marca la notificación como no leída
     */
    public void markAsUnread() {
        this.isRead = false;
        this.readAt = null;
    }
    
    /**
     * Desactiva la notificación (soft delete)
     */
    public void deactivate() {
        this.active = false;
    }
    
    /**
     * Verifica si la notificación ha expirado
     */
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }
    
    /**
     * Establece la prioridad de la notificación
     */
    public void setPriorityLevel(NotificationPriority priority) {
        this.priority = priority;
    }
    
    /**
     * Establece la información de la entidad relacionada
     */
    public void setRelatedEntity(String entityType, Long entityId, String actionUrl) {
        this.relatedEntityType = entityType;
        this.relatedEntityId = entityId;
        this.actionUrl = actionUrl;
    }
}
