package com.drawnet.feed_service.domain.model.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCommentCommand(
    @NotNull(message = "Post ID is required")
    Long postId,
    
    @NotNull(message = "User ID is required")
    Long userId,
    
    @NotBlank(message = "Content cannot be empty")
    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    String content,
    
    Long parentCommentId // Para respuestas a comentarios
) {}