package com.drawnet.feed_service.interfaces.rest.transform;

import com.drawnet.feed_service.domain.model.entities.Repost;
import com.drawnet.feed_service.interfaces.rest.resources.RepostResource;

public class RepostResourceFromEntityAssembler {

    public static RepostResource toResourceFromEntity(Repost entity) {
        return new RepostResource(
                entity.getId(),
                entity.getUserId(),
                entity.getOriginalPost().getId(),
                entity.getComment(),
                entity.getRepostDate(),
                entity.isActive(),
                PostResourceFromEntityAssembler.toResourceFromEntity(entity.getOriginalPost())
        );
    }
}