package com.drawnet.feed_service.interfaces.rest.resources;

import com.drawnet.feed_service.domain.model.entities.MediaType;

import java.time.LocalDateTime;

public record MediaResource(
    Long id,
    String fileUrl,
    String originalFilename,
    MediaType mediaType,
    Long fileSize,
    String fileExtension,
    Long postId,
    LocalDateTime uploadDate,
    String altText,
    String formattedFileSize
) {}