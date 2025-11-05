package com.drawnet.feed_service.interfaces.rest.resources;

import java.time.LocalDateTime;

public record CommentResource(
    Long id,
    Long userId,
    String content,
    Long postId,
    Long parentCommentId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    boolean active,
    int repliesCount,
    boolean isReply
) {}