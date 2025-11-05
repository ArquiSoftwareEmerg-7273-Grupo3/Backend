package com.drawnet.feed_service.interfaces.rest.resources;

import com.drawnet.feed_service.domain.model.entities.ReactionType;
import jakarta.validation.constraints.NotNull;

public record CreateReactionResource(
    @NotNull(message = "Reaction type is required")
    ReactionType reactionType
) {}