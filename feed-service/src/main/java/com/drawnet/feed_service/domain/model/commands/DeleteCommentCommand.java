package com.drawnet.feed_service.domain.model.commands;

import jakarta.validation.constraints.NotNull;

public record DeleteCommentCommand(
    @NotNull(message = "Comment ID is required")
    Long commentId,
    
    @NotNull(message = "User ID is required")
    Long userId // Para verificar autorización
) {}