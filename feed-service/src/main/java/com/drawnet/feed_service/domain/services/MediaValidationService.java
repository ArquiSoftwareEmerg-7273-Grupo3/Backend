package com.drawnet.feed_service.domain.services;

import com.drawnet.feed_service.domain.entities.Post;
import com.drawnet.feed_service.domain.entities.Media;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio de dominio para validación y procesamiento de contenido multimedia
 */
@Service
public class MediaValidationService {

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/jpeg", "image/png", "image/gif", "image/webp"
    );
    
    private static final Set<String> ALLOWED_VIDEO_TYPES = Set.of(
            "video/mp4", "video/webm", "video/mov", "video/avi"
    );
    
    private static final Set<String> ALLOWED_AUDIO_TYPES = Set.of(
            "audio/mp3", "audio/wav", "audio/ogg", "audio/m4a"
    );
    
    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final long MAX_VIDEO_SIZE = 100 * 1024 * 1024; // 100MB
    private static final long MAX_AUDIO_SIZE = 50 * 1024 * 1024; // 50MB
    
    private static final int MAX_MEDIA_PER_POST = 10;
    private static final int MAX_WIDTH = 4096;
    private static final int MAX_HEIGHT = 4096;

    /**
     * Validar archivo multimedia antes del upload
     */
    public MediaValidationResult validateMediaFile(MultipartFile file) {
        MediaValidationResult result = new MediaValidationResult();
        
        if (file == null || file.isEmpty()) {
            result.addError("FILE_EMPTY", "El archivo está vacío");
            return result;
        }
        
        String contentType = file.getContentType();
        if (contentType == null) {
            result.addError("UNKNOWN_TYPE", "Tipo de archivo desconocido");
            return result;
        }
        
        // Validar tipo de archivo
        Media.MediaType mediaType = determineMediaType(contentType);
        if (mediaType == null) {
            result.addError("UNSUPPORTED_TYPE", "Tipo de archivo no soportado: " + contentType);
            return result;
        }
        
        // Validar tamaño
        if (!validateFileSize(file.getSize(), mediaType)) {
            result.addError("FILE_TOO_LARGE", "Archivo demasiado grande para el tipo " + mediaType);
            return result;
        }
        
        // Validar nombre de archivo
        String filename = file.getOriginalFilename();
        if (filename == null || filename.trim().isEmpty()) {
            result.addError("INVALID_FILENAME", "Nombre de archivo inválido");
            return result;
        }
        
        // Validar extensión
        if (!validateFileExtension(filename, contentType)) {
            result.addError("EXTENSION_MISMATCH", "La extensión no coincide con el tipo de archivo");
            return result;
        }
        
        result.setValid(true);
        result.setMediaType(mediaType);
        return result;
    }

    /**
     * Validar múltiples archivos para un post
     */
    public MediaCollectionValidationResult validateMediaCollection(List<MultipartFile> files) {
        MediaCollectionValidationResult result = new MediaCollectionValidationResult();
        
        if (files == null || files.isEmpty()) {
            result.setValid(true);
            return result;
        }
        
        if (files.size() > MAX_MEDIA_PER_POST) {
            result.addError("TOO_MANY_FILES", 
                          String.format("Máximo %d archivos por post", MAX_MEDIA_PER_POST));
            return result;
        }
        
        // Validar cada archivo individualmente
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            MediaValidationResult fileResult = validateMediaFile(file);
            
            if (!fileResult.isValid()) {
                result.addFileError(i, fileResult.getErrors());
            } else {
                result.addValidFile(i, fileResult.getMediaType());
            }
        }
        
        // Validar combinación de tipos
        validateMediaTypeCombination(result);
        
        return result;
    }

    /**
     * Generar nombre de archivo único y seguro
     */
    public String generateUniqueFilename(String originalFilename, String userId) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String randomSuffix = UUID.randomUUID().toString().substring(0, 8);
        String extension = getFileExtension(originalFilename);
        
        return String.format("%s_%s_%s.%s", userId, timestamp, randomSuffix, extension);
    }

    /**
     * Validar integridad de medios existentes en un post
     */
    public MediaIntegrityResult validatePostMediaIntegrity(Post post) {
        MediaIntegrityResult result = new MediaIntegrityResult();
        
        if (post.getMediaFiles() == null || post.getMediaFiles().isEmpty()) {
            result.setValid(true);
            return result;
        }
        
        for (Media media : post.getMediaFiles()) {
            MediaIntegrityCheck check = validateMediaIntegrity(media);
            result.addMediaCheck(media.getId(), check);
            
            if (!check.isValid()) {
                result.setValid(false);
            }
        }
        
        return result;
    }

    /**
     * Calcular métricas de uso de storage
     */
    public StorageMetrics calculateStorageMetrics(String userId) {
        StorageMetrics metrics = new StorageMetrics();
        
        // Este método requeriría acceso a repositorios o servicios de storage
        // Por ahora retornamos métricas básicas
        metrics.setUserId(userId);
        metrics.setTotalFiles(0);
        metrics.setTotalSize(0L);
        metrics.setQuotaUsed(0.0);
        
        return metrics;
    }

    // Métodos auxiliares privados
    
    private Media.MediaType determineMediaType(String contentType) {
        if (ALLOWED_IMAGE_TYPES.contains(contentType)) {
            return Media.MediaType.IMAGE;
        } else if (ALLOWED_VIDEO_TYPES.contains(contentType)) {
            return Media.MediaType.VIDEO;
        } else if (ALLOWED_AUDIO_TYPES.contains(contentType)) {
            return Media.MediaType.AUDIO;
        }
        return null;
    }
    
    private boolean validateFileSize(long size, Media.MediaType mediaType) {
        return switch (mediaType) {
            case IMAGE -> size <= MAX_IMAGE_SIZE;
            case VIDEO -> size <= MAX_VIDEO_SIZE;
            case AUDIO -> size <= MAX_AUDIO_SIZE;
        };
    }
    
    private boolean validateFileExtension(String filename, String contentType) {
        String extension = getFileExtension(filename).toLowerCase();
        
        return switch (contentType) {
            case "image/jpeg" -> "jpg".equals(extension) || "jpeg".equals(extension);
            case "image/png" -> "png".equals(extension);
            case "image/gif" -> "gif".equals(extension);
            case "image/webp" -> "webp".equals(extension);
            case "video/mp4" -> "mp4".equals(extension);
            case "video/webm" -> "webm".equals(extension);
            case "video/mov" -> "mov".equals(extension);
            case "video/avi" -> "avi".equals(extension);
            case "audio/mp3" -> "mp3".equals(extension);
            case "audio/wav" -> "wav".equals(extension);
            case "audio/ogg" -> "ogg".equals(extension);
            case "audio/m4a" -> "m4a".equals(extension);
            default -> false;
        };
    }
    
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
    
    private void validateMediaTypeCombination(MediaCollectionValidationResult result) {
        Set<Media.MediaType> types = result.getValidFiles().values().stream()
                .collect(Collectors.toSet());
        
        // Reglas de negocio para combinaciones
        if (types.contains(Media.MediaType.VIDEO) && types.size() > 1) {
            result.addError("MIXED_WITH_VIDEO", "No se pueden combinar videos con otros tipos de media");
        }
        
        if (types.contains(Media.MediaType.AUDIO) && types.contains(Media.MediaType.IMAGE)) {
            // Audio + imágenes está permitido
        }
    }
    
    private MediaIntegrityCheck validateMediaIntegrity(Media media) {
        MediaIntegrityCheck check = new MediaIntegrityCheck();
        check.setMediaId(media.getId());
        
        // Verificar que el archivo existe
        Path filePath = Paths.get(media.getUrl());
        if (!Files.exists(filePath)) {
            check.addIssue("FILE_NOT_FOUND", "Archivo no encontrado en el sistema");
        }
        
        // Verificar metadatos
        if (media.getFileSize() == null || media.getFileSize() <= 0) {
            check.addIssue("INVALID_SIZE", "Tamaño de archivo inválido");
        }
        
        if (media.getMimeType() == null || media.getMimeType().trim().isEmpty()) {
            check.addIssue("MISSING_MIME_TYPE", "Tipo MIME faltante");
        }
        
        check.setValid(check.getIssues().isEmpty());
        return check;
    }

    // Clases para resultados de validación
    
    public static class MediaValidationResult {
        private boolean valid = false;
        private Media.MediaType mediaType;
        private final Map<String, String> errors = new HashMap<>();
        
        public void addError(String code, String message) {
            errors.put(code, message);
        }
        
        // Getters y setters
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public Media.MediaType getMediaType() { return mediaType; }
        public void setMediaType(Media.MediaType mediaType) { this.mediaType = mediaType; }
        public Map<String, String> getErrors() { return errors; }
    }
    
    public static class MediaCollectionValidationResult {
        private boolean valid = true;
        private final Map<String, String> errors = new HashMap<>();
        private final Map<Integer, Map<String, String>> fileErrors = new HashMap<>();
        private final Map<Integer, Media.MediaType> validFiles = new HashMap<>();
        
        public void addError(String code, String message) {
            errors.put(code, message);
            valid = false;
        }
        
        public void addFileError(int fileIndex, Map<String, String> fileErrors) {
            this.fileErrors.put(fileIndex, fileErrors);
            valid = false;
        }
        
        public void addValidFile(int fileIndex, Media.MediaType mediaType) {
            validFiles.put(fileIndex, mediaType);
        }
        
        // Getters
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public Map<String, String> getErrors() { return errors; }
        public Map<Integer, Map<String, String>> getFileErrors() { return fileErrors; }
        public Map<Integer, Media.MediaType> getValidFiles() { return validFiles; }
    }
    
    public static class MediaIntegrityResult {
        private boolean valid = true;
        private final Map<String, MediaIntegrityCheck> mediaChecks = new HashMap<>();
        
        public void addMediaCheck(String mediaId, MediaIntegrityCheck check) {
            mediaChecks.put(mediaId, check);
        }
        
        // Getters y setters
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public Map<String, MediaIntegrityCheck> getMediaChecks() { return mediaChecks; }
    }
    
    public static class MediaIntegrityCheck {
        private String mediaId;
        private boolean valid = true;
        private final Map<String, String> issues = new HashMap<>();
        
        public void addIssue(String code, String description) {
            issues.put(code, description);
            valid = false;
        }
        
        // Getters y setters
        public String getMediaId() { return mediaId; }
        public void setMediaId(String mediaId) { this.mediaId = mediaId; }
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public Map<String, String> getIssues() { return issues; }
    }
    
    public static class StorageMetrics {
        private String userId;
        private int totalFiles;
        private long totalSize;
        private double quotaUsed;
        
        // Getters y setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }
        public int getTotalFiles() { return totalFiles; }
        public void setTotalFiles(int totalFiles) { this.totalFiles = totalFiles; }
        public long getTotalSize() { return totalSize; }
        public void setTotalSize(long totalSize) { this.totalSize = totalSize; }
        public double getQuotaUsed() { return quotaUsed; }
        public void setQuotaUsed(double quotaUsed) { this.quotaUsed = quotaUsed; }
    }
}