package com.drawnet.feed_service.interfaces.rest.transform;

import com.drawnet.feed_service.domain.model.aggregates.Post;
import com.drawnet.feed_service.interfaces.rest.resources.PostResource;

public class PostResourceFromEntityAssembler {

    public static PostResource toResourceFromEntity(Post entity) {
        return new PostResource(
                entity.getId(),
                entity.getAuthorId(),
                entity.getContent(),
                entity.getTagsUnmodifiable(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isActive(),
                entity.getReactionsCount(),
                entity.getCommentsCount(),
                entity.getRepostsCount(),
                entity.getViewsCount(),
                entity.hasMedia(),
                entity.getEngagementRate()
        );
    }
}