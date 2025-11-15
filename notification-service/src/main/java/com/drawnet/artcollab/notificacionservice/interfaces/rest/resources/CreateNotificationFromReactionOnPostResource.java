package com.drawnet.artcollab.notificacionservice.interfaces.rest.resources;

public record CreateNotificationFromReactionOnPostResource(
        Long postId,
        Long reactorId,
        String reactionType   // "LIKE", "LOVE", etc.
) {
}
