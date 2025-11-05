package com.drawnet.feed_service.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CreatePostResource(
    @NotBlank(message = "Content cannot be empty")
    @Size(max = 2000, message = "Content cannot exceed 2000 characters")
    String content,
    
    Set<String> tags
) {}