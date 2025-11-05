package com.drawnet.feed_service.interfaces.rest.transform;

import com.drawnet.feed_service.domain.model.entities.Comment;
import com.drawnet.feed_service.interfaces.rest.resources.CommentResource;

public class CommentResourceFromEntityAssembler {

    public static CommentResource toResourceFromEntity(Comment entity) {
        return new CommentResource(
                entity.getId(),
                entity.getUserId(),
                entity.getContent(),
                entity.getPost().getId(),
                entity.getParentComment() != null ? entity.getParentComment().getId() : null,
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isActive(),
                entity.getRepliesCount(),
                entity.isReply()
        );
    }
}