package com.drawnet.artcollab.notificacionservice.domain.model.commands;

public record CreateNotificationFromCommentCommand(
        Long postAuthorId,     // quién recibe la notificación
        Long commenterId,      // quién hizo el comentario
        Long postId,
        Long commentId,
        String commentContent
) {
}
