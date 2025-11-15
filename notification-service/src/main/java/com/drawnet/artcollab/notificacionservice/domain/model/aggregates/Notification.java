package com.drawnet.artcollab.notificacionservice.domain.model.aggregates;

import com.drawnet.artcollab.notificacionservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import com.drawnet.artcollab.notificacionservice.domain.model.valueobjects.NotificationSourceType;
import com.drawnet.artcollab.notificacionservice.domain.model.valueobjects.NotificationStatus;
import com.drawnet.artcollab.notificacionservice.domain.model.valueobjects.NotificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class Notification extends AuditableAbstractAggregateRoot<Notification> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Recipient ID cannot be null")
    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;        // usuario que recibe la notificación (dueño del post/comentario)

    @NotNull(message = "Actor ID cannot be null")
    @Column(name = "actor_id", nullable = false)
    private Long actorId;             // usuario que comenta o reacciona

    @NotNull(message = "Notification type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 40)
    private NotificationType type;

    @NotNull(message = "Notification source type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false, length = 20)
    private NotificationSourceType sourceType; // POST o COMMENT

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "comment_id")
    private Long commentId;

    @Size(max = 255, message = "Message cannot exceed 255 characters")
    @Column(name = "message", length = 255)
    private String message;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private NotificationStatus status = NotificationStatus.PENDIENTE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    public Notification(
            Long recipientId,
            Long actorId,
            NotificationType type,
            NotificationSourceType sourceType,
            Long postId,
            Long commentId,
            String message
    ) {
        this.recipientId = recipientId;
        this.actorId = actorId;
        this.type = type;
        this.sourceType = sourceType;
        this.postId = postId;
        this.commentId = commentId;
        this.message = message;
        this.status = NotificationStatus.PENDIENTE;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
