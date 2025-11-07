package com.drawnet.feed_service.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentResource(
    @NotBlank(message = "Content cannot be empty")
    @Size(max = 1000, message = "Comment cannot exceed 1000 characters")
    @Schema(description = "Content of the comment", example = "Great post!")
    String content,
    
    @Schema(description = "ID of parent comment if this is a reply. Leave null for direct comments to the post", 
            example = "null", 
            nullable = true)
    Long parentCommentId
) {}