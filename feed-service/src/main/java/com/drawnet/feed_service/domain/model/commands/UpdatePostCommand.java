package com.drawnet.feed_service.domain.model.commands;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdatePostCommand(
    @NotNull(message = "Post ID is required")
    Long postId,
    
    @NotBlank(message = "Content cannot be empty")
    @Size(max = 2000, message = "Content cannot exceed 2000 characters")
    String content,
    
    Set<String> tags
) {
    public UpdatePostCommand {
        // Normalizar tags si existen
        if (tags != null) {
            tags = tags.stream()
                .filter(tag -> tag != null && !tag.trim().isEmpty())
                .map(tag -> tag.trim().toLowerCase())
                .collect(java.util.stream.Collectors.toSet());
        }
    }
}