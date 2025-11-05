package com.drawnet.feed_service.domain.model.commands;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateRepostCommand(
    @NotNull(message = "Original post ID is required")
    Long originalPostId,
    
    @NotNull(message = "User ID is required")
    Long userId,
    
    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    String comment // Comentario opcional
) {}