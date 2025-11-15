package com.drawnet.artcollab.notificacionservice.interfaces.rest.resources;

public record CreateNotificationFromCommentResource(
        Long postId,
        Long commentId,
        Long commenterId,
        String commentContent
) {
}
