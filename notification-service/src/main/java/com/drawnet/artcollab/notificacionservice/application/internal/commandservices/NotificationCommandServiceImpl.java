package com.drawnet.artcollab.notificacionservice.application.internal.commandservices;

import com.drawnet.artcollab.notificacionservice.domain.model.aggregates.Notification;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.CreateNotificationCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.CreateNotificationFromCommentCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.CreateNotificationFromReactionOnPostCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.DeleteNotificationCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.MarkAllAsReadCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.MarkNotificationAsReadCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.valueobjects.NotificationType;
import com.drawnet.artcollab.notificacionservice.domain.services.NotificationCommandService;
import com.drawnet.artcollab.notificacionservice.infrastructure.persistence.jpa.repositories.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final NotificationRepository notificationRepository;

    @Override
    public Optional<Notification> handle(CreateNotificationCommand command) {
        Notification notification = new Notification(
                command.recipientUserId(),
                command.actorUserId(),
                command.type(),
                command.title(),
                command.message()
        );
        
        if (command.relatedEntityType() != null && command.relatedEntityId() != null) {
            notification.setRelatedEntity(
                command.relatedEntityType(),
                command.relatedEntityId(),
                command.actionUrl()
            );
        }
        
        if (command.priority() != null) {
            notification.setPriorityLevel(command.priority());
        }
        
        if (command.expiresAt() != null) {
            notification.setExpiresAt(command.expiresAt());
        }
        
        Notification saved = notificationRepository.save(notification);
        return Optional.of(saved);
    }

    private String buildCommentMessage(String content) {
        if (content == null || content.isBlank()) {
            return "Han comentado tu publicación.";
        }
        String snippet = content.length() > 40
                ? content.substring(0, 40) + "..."
                : content;
        return "Han comentado tu publicación: \"" + snippet + "\"";
    }

    @Override
    public Optional<Notification> handle(CreateNotificationFromCommentCommand command) {
        String message = buildCommentMessage(command.commentContent());
        String title = "Nuevo comentario";

        Notification notification = new Notification(
                command.postAuthorId(),
                command.commenterId(),
                NotificationType.NEW_COMMENT,
                title,
                message
        );
        
        notification.setRelatedEntity("POST", command.postId(), "/posts/" + command.postId());
        
        Notification saved = notificationRepository.save(notification);
        return Optional.of(saved);
    }

    @Override
    public Optional<Notification> handle(CreateNotificationFromReactionOnPostCommand command) {
        String message = "Han reaccionado (" + command.reactionType() + ") a tu publicación.";
        String title = "Nueva reacción";

        Notification notification = new Notification(
                command.postAuthorId(),
                command.reactorId(),
                NotificationType.NEW_LIKE,
                title,
                message
        );
        
        notification.setRelatedEntity("POST", command.postId(), "/posts/" + command.postId());
        
        Notification saved = notificationRepository.save(notification);
        return Optional.of(saved);
    }

    @Override
    public Optional<Notification> handle(MarkNotificationAsReadCommand command) {
        Notification notification = notificationRepository.findById(command.notificationId())
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        notification.markAsRead();
        
        return Optional.of(notification);
    }

    @Override
    public int handle(MarkAllAsReadCommand command) {
        return notificationRepository.markAllAsReadByUserId(command.userId(), LocalDateTime.now());
    }

    @Override
    public boolean handle(DeleteNotificationCommand command) {
        Optional<Notification> notificationOpt = notificationRepository.findById(command.notificationId());
        
        if (notificationOpt.isEmpty()) {
            return false;
        }
        
        Notification notification = notificationOpt.get();
        notification.deactivate();
        
        return true;
    }
}
