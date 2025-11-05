package com.drawnet.feed_service.domain.model.commands;

import com.drawnet.feed_service.domain.model.entities.MediaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UploadMediaCommand(
    @NotNull(message = "Post ID is required")
    Long postId,
    
    @NotBlank(message = "File URL is required")
    @Size(max = 500, message = "File URL cannot exceed 500 characters")
    String fileUrl,
    
    @NotBlank(message = "Original filename is required")
    @Size(max = 255, message = "Filename cannot exceed 255 characters")
    String originalFilename,
    
    @NotNull(message = "Media type is required")
    MediaType mediaType,
    
    Long fileSize,
    
    @Size(max = 10, message = "File extension cannot exceed 10 characters")
    String fileExtension,
    
    @Size(max = 255, message = "Alt text cannot exceed 255 characters")
    String altText
) {}