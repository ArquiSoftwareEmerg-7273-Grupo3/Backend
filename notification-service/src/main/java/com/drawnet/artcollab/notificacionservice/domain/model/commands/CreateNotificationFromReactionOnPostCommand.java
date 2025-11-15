package com.drawnet.artcollab.notificacionservice.domain.model.commands;

public record CreateNotificationFromReactionOnPostCommand(
        Long postAuthorId,     // dueño del post
        Long reactorId,        // quién reaccionó
        Long postId,
        String reactionType   // "LIKE", "LOVE", etc.
) {
}
