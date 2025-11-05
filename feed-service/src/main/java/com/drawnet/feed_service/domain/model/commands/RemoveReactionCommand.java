package com.drawnet.feed_service.domain.model.commands;

import jakarta.validation.constraints.NotNull;

public record RemoveReactionCommand(
    @NotNull(message = "Post ID is required")
    Long postId,
    
    @NotNull(message = "User ID is required")
    Long userId
) {}