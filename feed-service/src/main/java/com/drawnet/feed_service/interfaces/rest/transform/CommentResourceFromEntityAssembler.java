package com.drawnet.feed_service.interfaces.rest.transform;

import com.drawnet.feed_service.domain.model.entities.Comment;
import com.drawnet.feed_service.interfaces.rest.resources.CommentResource;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class CommentResourceFromEntityAssembler {

    public static CommentResource toResourceFromEntity(Comment entity) {
        return new CommentResource(
                entity.getId(),
                entity.getUserId(),
                entity.getContent(),
                entity.getPost().getId(),
                entity.getParentComment() != null ? entity.getParentComment().getId() : null,
                entity.getCreatedAt() != null ? 
                    LocalDateTime.ofInstant(entity.getCreatedAt().toInstant(), ZoneId.systemDefault()) : null,
                entity.getUpdatedAt() != null ? 
                    LocalDateTime.ofInstant(entity.getUpdatedAt().toInstant(), ZoneId.systemDefault()) : null,
                entity.isActive(),
                entity.getRepliesCount(),
                entity.isReply()
        );
    }
}