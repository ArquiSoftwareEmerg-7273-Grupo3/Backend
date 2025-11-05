package com.drawnet.feed_service.domain.model.querys;

import java.time.LocalDateTime;
import java.util.List;

public record GetPostsQuery(
    Long authorId,
    List<String> tags,
    String contentFilter,
    List<Long> followingUserIds,
    LocalDateTime since,
    PostSortBy sortBy,
    int page,
    int size
) {
    public GetPostsQuery {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        if (size > 100) size = 100; // Límite máximo
    }
    
    public enum PostSortBy {
        NEWEST,
        OLDEST,
        MOST_LIKED,
        MOST_COMMENTED,
        TRENDING
    }
}