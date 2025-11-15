package com.drawnet.artcollab.notificacionservice.application.internal.commandservices;

import com.drawnet.artcollab.notificacionservice.domain.model.aggregates.Notification;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.CreateNotificationFromCommentCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.CreateNotificationFromReactionOnPostCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.MarkNotificationAsReadCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.valueobjects.NotificationSourceType;
import com.drawnet.artcollab.notificacionservice.domain.model.valueobjects.NotificationStatus;
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

        // 1. Construir el mensaje a partir del contenido del comentario
        String message = buildCommentMessage(command.commentContent());

        // 2. Crear la entidad Notification usando TU constructor público
        Notification notification = new Notification(
                command.postAuthorId(),                 // recipientId (dueño del post)
                command.commenterId(),                  // actorId (quien comenta)
                NotificationType.COMENTARIO_EN_POST,    // type
                NotificationSourceType.POST,            // sourceType
                command.postId(),                       // postId
                command.commentId(),                    // commentId
                message                                 // message
        );
        // status = PENDIENTE se setea en el constructor de Notification

        // 3. Guardar en BD
        Notification saved = notificationRepository.save(notification);

        return Optional.of(saved);
    }

    @Override
    public Optional<Notification> handle(CreateNotificationFromReactionOnPostCommand command) {

        // 1. Construir el mensaje para la reacción
        String message = "Han reaccionado (" + command.reactionType() + ") a tu publicación.";

        // 2. Crear Notification
        Notification notification = new Notification(
                command.postAuthorId(),                 // recipientId (dueño del post)
                command.reactorId(),                    // actorId (quien reacciona)
                NotificationType.REACCION_EN_POST,      // type
                NotificationSourceType.POST,            // sourceType
                command.postId(),                       // postId
                null,                                   // commentId (no aplica)
                message                                 // message
        );

        // 3. Guardar
        Notification saved = notificationRepository.save(notification);

        return Optional.of(saved);
    }

    @Override
    public Optional<Notification> handle(MarkNotificationAsReadCommand command) {

        // 1. Buscar la notificación
        Notification notification = notificationRepository.findById(command.notificationId())
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        // 2. (Opcional, pero recomendado) Validar que el usuario sea el dueño
        //if (!notification.getRecipientId().equals(command.userId())) {
        //    throw new IllegalStateException("User is not the owner of this notification");
        //}

        // 3. Actualizar estado y timestamps
        notification.setStatus(NotificationStatus.LEIDA);
        notification.setReadAt(LocalDateTime.now());
        // updatedAt se actualizará con @PreUpdate en la entidad Notification

        // Al estar en @Transactional, el cambio se sincroniza al final.
        // Podrías llamar explícitamente a save(), pero no es estrictamente necesario:
        // notificationRepository.save(notification);

        return Optional.of(notification);
    }
}
