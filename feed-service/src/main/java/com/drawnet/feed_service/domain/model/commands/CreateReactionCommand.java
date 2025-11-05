package com.drawnet.feed_service.domain.model.commands;

import com.drawnet.feed_service.domain.model.entities.ReactionType;
import jakarta.validation.constraints.NotNull;

public record CreateReactionCommand(
    @NotNull(message = "Post ID is required")
    Long postId,
    
    @NotNull(message = "User ID is required")
    Long userId,
    
    @NotNull(message = "Reaction type is required")
    ReactionType reactionType
) {}