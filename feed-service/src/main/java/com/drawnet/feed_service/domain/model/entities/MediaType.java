package com.drawnet.feed_service.domain.model.entities;

import java.util.Arrays;
import java.util.List;

public enum MediaType {
    IMAGE("image", Arrays.asList("jpg", "jpeg", "png", "gif", "webp")),
    VIDEO("video", Arrays.asList("mp4", "avi", "mov", "wmv", "flv")),
    DOCUMENT("document", Arrays.asList("pdf", "doc", "docx", "txt"));
    
    private final String category;
    private final List<String> allowedExtensions;
    
    MediaType(String category, List<String> allowedExtensions) {
        this.category = category;
        this.allowedExtensions = allowedExtensions;
    }
    
    public String getCategory() {
        return category;
    }
    
    public List<String> getAllowedExtensions() {
        return allowedExtensions;
    }
    
    public boolean isValidExtension(String extension) {
        return allowedExtensions.contains(extension.toLowerCase());
    }
    
    public static MediaType fromExtension(String extension) {
        String ext = extension.toLowerCase().replace(".", "");
        for (MediaType type : values()) {
            if (type.isValidExtension(ext)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported file extension: " + extension);
    }
}