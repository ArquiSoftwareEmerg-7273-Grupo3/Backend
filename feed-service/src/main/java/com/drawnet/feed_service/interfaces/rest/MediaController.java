package com.drawnet.feed_service.interfaces.rest;

import com.drawnet.feed_service.application.services.FileStorageService;
import com.drawnet.feed_service.domain.model.entities.Media;
import com.drawnet.feed_service.domain.model.entities.MediaType;
import com.drawnet.feed_service.infrastructure.persistence.jpa.repositories.MediaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para gestionar la subida de archivos multimedia
 */
@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
@Tag(name = "Media", description = "Gestión de archivos multimedia (imágenes, videos)")
@Slf4j
public class MediaController {

    private final FileStorageService fileStorageService;
    private final MediaRepository mediaRepository;

    /**
     * Subir una imagen o video
     * 
     * @param file Archivo a subir
     * @return Información del archivo subido
     */
    @PostMapping(value = "/upload", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Subir imagen o video", description = "Sube un archivo multimedia y retorna la URL para usarlo")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "altText", required = false) String altText
    ) {
        try {
            log.info("📤 Recibiendo archivo: {} ({})", file.getOriginalFilename(), file.getContentType());

            // Validar que el archivo no esté vacío
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El archivo está vacío"));
            }

            // Obtener información del archivo
            FileStorageService.FileInfo fileInfo = fileStorageService.getFileInfo(file);

            // Guardar archivo en el sistema de archivos
            String fileUrl = fileStorageService.storeFile(file);

            // Determinar tipo de media
            MediaType mediaType = determineMediaType(file.getContentType());

            // Crear entidad Media
            Media media = new Media(
                    fileUrl,
                    fileInfo.originalFilename(),
                    mediaType,
                    fileInfo.size(),
                    fileInfo.extension()
            );

            if (altText != null && !altText.isBlank()) {
                media.setAltText(altText);
            }

            // Guardar en base de datos
            Media savedMedia = mediaRepository.save(media);

            log.info("✅ Archivo guardado exitosamente: ID={}, URL={}", savedMedia.getId(), fileUrl);

            // Construir respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("id", savedMedia.getId());
            response.put("url", fileUrl);
            response.put("originalFilename", savedMedia.getOriginalFilename());
            response.put("mediaType", savedMedia.getMediaType());
            response.put("fileSize", savedMedia.getFileSize());
            response.put("fileExtension", savedMedia.getFileExtension());
            response.put("formattedSize", savedMedia.getFormattedFileSize());
            response.put("uploadDate", savedMedia.getUploadDate());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException ex) {
            log.error("❌ Error al subir archivo: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * Eliminar un archivo
     * 
     * @param id ID del archivo
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar archivo", description = "Elimina un archivo multimedia del sistema")
    public ResponseEntity<Map<String, String>> deleteFile(@PathVariable Long id) {
        try {
            Media media = mediaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));

            // Eliminar archivo del sistema
            fileStorageService.deleteFile(media.getFileUrl());

            // Eliminar de base de datos
            mediaRepository.delete(media);

            log.info("🗑️ Archivo eliminado: ID={}", id);

            return ResponseEntity.ok(Map.of("message", "Archivo eliminado exitosamente"));

        } catch (RuntimeException ex) {
            log.error("❌ Error al eliminar archivo: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * Obtener información de un archivo
     * 
     * @param id ID del archivo
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener información del archivo", description = "Obtiene la información de un archivo multimedia")
    public ResponseEntity<?> getFileInfo(@PathVariable Long id) {
        try {
            Media media = mediaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));

            Map<String, Object> response = new HashMap<>();
            response.put("id", media.getId());
            response.put("url", media.getFileUrl());
            response.put("originalFilename", media.getOriginalFilename());
            response.put("mediaType", media.getMediaType());
            response.put("fileSize", media.getFileSize());
            response.put("fileExtension", media.getFileExtension());
            response.put("formattedSize", media.getFormattedFileSize());
            response.put("uploadDate", media.getUploadDate());
            response.put("altText", media.getAltText());

            return ResponseEntity.ok(response);

        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    /**
     * Determinar tipo de media según content type
     */
    private MediaType determineMediaType(String contentType) {
        if (contentType == null) {
            return MediaType.IMAGE;
        }

        if (contentType.startsWith("image/")) {
            return MediaType.IMAGE;
        } else if (contentType.startsWith("video/")) {
            return MediaType.VIDEO;
        } else {
            return MediaType.DOCUMENT;
        }
    }
}
