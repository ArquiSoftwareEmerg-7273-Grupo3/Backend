package com.drawnet.artcollab.notificacionservice.interfaces.rest.transform;

import com.drawnet.artcollab.notificacionservice.domain.model.commands.CreateNotificationFromReactionOnPostCommand;
import com.drawnet.artcollab.notificacionservice.interfaces.rest.resources.CreateNotificationFromReactionOnPostResource;

public class CreateNotificationFromReactionOnPostCommandFromResourceAssembler {
    public static CreateNotificationFromReactionOnPostCommand toCommand(
            Long postAuthorId,
            CreateNotificationFromReactionOnPostResource resource
    ) {
        return new CreateNotificationFromReactionOnPostCommand(
                postAuthorId,
                resource.reactorId(),
                resource.postId(),
                resource.reactionType()
        );
    }
}
