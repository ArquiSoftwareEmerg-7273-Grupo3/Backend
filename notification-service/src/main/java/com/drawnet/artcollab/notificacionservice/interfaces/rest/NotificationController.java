package com.drawnet.artcollab.notificacionservice.interfaces.rest;

import com.drawnet.artcollab.notificacionservice.domain.model.aggregates.Notification;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.CreateNotificationFromCommentCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.CreateNotificationFromReactionOnPostCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.MarkNotificationAsReadCommand;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetAllNotificationsByUserQuery;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetNotificationByIdQuery;
import com.drawnet.artcollab.notificacionservice.domain.model.queries.GetUnreadNotificationsByUserQuery;
import com.drawnet.artcollab.notificacionservice.domain.services.NotificationCommandService;
import com.drawnet.artcollab.notificacionservice.domain.services.NotificationQueryService;
import com.drawnet.artcollab.notificacionservice.domain.model.commands.CreateNotificationCommand;
import com.drawnet.artcollab.notificacionservice.interfaces.rest.resources.CreateGenericNotificationResource;
import com.drawnet.artcollab.notificacionservice.interfaces.rest.resources.CreateNotificationFromCommentResource;
import com.drawnet.artcollab.notificacionservice.interfaces.rest.resources.CreateNotificationFromReactionOnPostResource;
import com.drawnet.artcollab.notificacionservice.interfaces.rest.resources.MarkNotificationAsReadResource;
import com.drawnet.artcollab.notificacionservice.interfaces.rest.resources.NotificationResource;
import com.drawnet.artcollab.notificacionservice.interfaces.rest.transform.CreateNotificationFromCommentCommandFromResourceAssembler;
import com.drawnet.artcollab.notificacionservice.interfaces.rest.transform.CreateNotificationFromReactionOnPostCommandFromResourceAssembler;
import com.drawnet.artcollab.notificacionservice.interfaces.rest.transform.MarkNotificationAsReadCommandFromResourceAssembler;
import com.drawnet.artcollab.notificacionservice.interfaces.rest.transform.NotificationResourceFromEntityAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationCommandService notificationCommandService;
    private final NotificationQueryService notificationQueryService;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(NotificationController.class);

    @GetMapping
    public ResponseEntity<List<NotificationResource>> getAllByUser(
            @RequestParam("userId") Long userId
    ) {
        var query = new GetAllNotificationsByUserQuery(userId);
        List<Notification> notifications = notificationQueryService.handle(query);

        List<NotificationResource> resources = notifications.stream()
                .map(NotificationResourceFromEntityAssembler::toResource)
                .collect(toList());

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResource>> getUnreadByUser(
            @RequestParam("userId") Long userId
    ) {
        var query = new GetUnreadNotificationsByUserQuery(userId);
        List<Notification> notifications = notificationQueryService.handle(query);

        List<NotificationResource> resources = notifications.stream()
                .map(NotificationResourceFromEntityAssembler::toResource)
                .collect(toList());

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResource> getById(
            @PathVariable Long notificationId
    ) {
        var query = new GetNotificationByIdQuery(notificationId);
        return notificationQueryService.handle(query)
                .map(NotificationResourceFromEntityAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/comments")
    public ResponseEntity<NotificationResource> createFromComment(
            @RequestParam("postAuthorId") Long postAuthorId,
            @RequestBody CreateNotificationFromCommentResource resource
    ) {
        CreateNotificationFromCommentCommand command =
                CreateNotificationFromCommentCommandFromResourceAssembler.toCommand(postAuthorId, resource);

        return notificationCommandService.handle(command)
                .map(NotificationResourceFromEntityAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping("/reactions")
    public ResponseEntity<NotificationResource> createFromReaction(
            @RequestParam("postAuthorId") Long postAuthorId,
            @RequestBody CreateNotificationFromReactionOnPostResource resource
    ) {
        CreateNotificationFromReactionOnPostCommand command =
                CreateNotificationFromReactionOnPostCommandFromResourceAssembler.toCommand(postAuthorId, resource);

        return notificationCommandService.handle(command)
                .map(NotificationResourceFromEntityAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResource> markAsRead(
            @PathVariable Long notificationId,
            @RequestBody MarkNotificationAsReadResource resource
    ) {
        MarkNotificationAsReadCommand command =
                MarkNotificationAsReadCommandFromResourceAssembler.toCommand(notificationId, resource);

        return notificationCommandService.handle(command)
                .map(NotificationResourceFromEntityAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/generic")
    public ResponseEntity<NotificationResource> createGenericNotification(
            @RequestBody CreateGenericNotificationResource resource
    ) {
        try {
            CreateNotificationCommand command = new CreateNotificationCommand(
                    resource.recipientUserId(),
                    resource.actorUserId(),
                    resource.type(),
                    resource.title(),
                    resource.message(),
                    resource.priority(),
                    resource.relatedEntityType(),
                    resource.relatedEntityId(),
                    resource.actionUrl(),
                    resource.metadata(),
                    resource.expiresAt()
            );

            return notificationCommandService.handle(command)
                    .map(NotificationResourceFromEntityAssembler::toResource)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.badRequest().build());
        } catch (Exception e) {
            logger.error("Error creating generic notification: ", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
