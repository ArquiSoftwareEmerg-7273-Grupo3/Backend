package com.drawnet.artcollab.notificacionservice.interfaces.rest.transform;

import com.drawnet.artcollab.notificacionservice.domain.model.commands.CreateNotificationFromCommentCommand;
import com.drawnet.artcollab.notificacionservice.interfaces.rest.resources.CreateNotificationFromCommentResource;

public class CreateNotificationFromCommentCommandFromResourceAssembler {
    public static CreateNotificationFromCommentCommand toCommand(
            Long postAuthorId,
            CreateNotificationFromCommentResource resource
    ) {
        return new CreateNotificationFromCommentCommand(
                postAuthorId,
                resource.commenterId(),
                resource.postId(),
                resource.commentId(),
                resource.commentContent()
        );
    }

}
