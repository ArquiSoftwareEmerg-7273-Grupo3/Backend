package com.drawnet.feed_service.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.Set;

public record PostResource(
    Long id,
    Long authorId,
    String content,
    Set<String> tags,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    boolean active,
    Integer reactionsCount,
    Integer commentsCount,
    Integer repostsCount,
    Integer viewsCount,
    boolean hasMedia,
    double engagementRate
) {}