package com.drawnet.feed_service.interfaces.rest.transform;

import com.drawnet.feed_service.domain.model.entities.Media;
import com.drawnet.feed_service.interfaces.rest.resources.MediaResource;

public class MediaResourceFromEntityAssembler {

    public static MediaResource toResourceFromEntity(Media entity) {
        return new MediaResource(
                entity.getId(),
                entity.getFileUrl(),
                entity.getOriginalFilename(),
                entity.getMediaType(),
                entity.getFileSize(),
                entity.getFileExtension(),
                entity.getPost() != null ? entity.getPost().getId() : null,
                entity.getUploadDate(),
                entity.getAltText(),
                entity.getFormattedFileSize()
        );
    }
}