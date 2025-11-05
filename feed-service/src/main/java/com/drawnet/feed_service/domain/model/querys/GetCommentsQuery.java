package com.drawnet.feed_service.domain.model.querys;

public record GetCommentsQuery(
    Long postId,
    Long parentCommentId,
    Long userId,
    int page,
    int size
) {
    public GetCommentsQuery {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        if (size > 50) size = 50; // Límite máximo para comentarios
    }
}