package com.drawnet.feed_service.interfaces.rest.resources;

import java.time.LocalDateTime;

public record RepostResource(
    Long id,
    Long userId,
    Long originalPostId,
    String comment,
    LocalDateTime repostDate,
    boolean active,
    PostResource originalPost
) {}