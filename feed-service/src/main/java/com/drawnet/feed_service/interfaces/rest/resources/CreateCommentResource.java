package com.drawnet.feed_service.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentResource(
    @NotBlank(message = "Content cannot be empty")
    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    String content,
    
    Long parentCommentId // Para respuestas
) {}