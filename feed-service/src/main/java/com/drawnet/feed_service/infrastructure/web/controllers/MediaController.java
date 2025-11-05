package com.drawnet.feed_service.infrastructure.web.controllers;

import com.drawnet.feed_service.application.commands.MediaCommand;
import com.drawnet.feed_service.application.queries.MediaQuery;
import com.drawnet.feed_service.domain.entities.Media;
import com.drawnet.feed_service.domain.services.MediaValidationService;
import com.drawnet.feed_service.infrastructure.web.dto.MediaResource;
import com.drawnet.feed_service.infrastructure.web.assemblers.MediaResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Controlador REST para gestión de archivos multimedia
 */
@RestController
@RequestMapping("/api/v1/media")
@Tag(name = "Media", description = "Gestión de archivos multimedia")
public class MediaController {

    private final MediaCommand mediaCommand;
    private final MediaQuery mediaQuery;
    private final MediaValidationService mediaValidationService;
    private final MediaResourceFromEntityAssembler mediaAssembler;

    public MediaController(MediaCommand mediaCommand,
                          MediaQuery mediaQuery,
                          MediaValidationService mediaValidationService,
                          MediaResourceFromEntityAssembler mediaAssembler) {
        this.mediaCommand = mediaCommand;
        this.mediaQuery = mediaQuery;
        this.mediaValidationService = mediaValidationService;
        this.mediaAssembler = mediaAssembler;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Subir archivo multimedia", 
               description = "Subir uno o múltiples archivos multimedia al sistema")
    @ApiResponse(responseCode = "201", description = "Archivo(s) subido(s) exitosamente")
    @ApiResponse(responseCode = "400", description = "Error de validación")
    public ResponseEntity<?> uploadMedia(
            @Parameter(description = "Archivos multimedia a subir")
            @RequestParam("files") List<MultipartFile> files,
            @Parameter(description = "ID del usuario")
            @RequestParam("userId") String userId,
            @Parameter(description = "ID del post (opcional)")
            @RequestParam(value = "postId", required = false) String postId) {
        
        try {
            // Validar archivos
            MediaValidationService.MediaCollectionValidationResult validation = 
                mediaValidationService.validateMediaCollection(files);
            
            if (!validation.isValid()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", "Error de validación");
                errorResponse.put("errors", validation.getErrors());
                errorResponse.put("fileErrors", validation.getFileErrors());
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Procesar upload
            List<Media> uploadedMedia = mediaCommand.uploadMultipleFiles(files, userId, postId);
            List<MediaResource> resources = uploadedMedia.stream()
                    .map(mediaAssembler::toModel)
                    .toList();

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Archivos subidos exitosamente");
            response.put("count", resources.size());
            response.put("media", resources);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error interno del servidor");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{mediaId}")
    @Operation(summary = "Obtener información de archivo multimedia")
    @ApiResponse(responseCode = "200", description = "Información del archivo")
    @ApiResponse(responseCode = "404", description = "Archivo no encontrado")
    public ResponseEntity<MediaResource> getMediaInfo(
            @Parameter(description = "ID del archivo multimedia")
            @PathVariable String mediaId) {
        
        Media media = mediaQuery.findById(mediaId);
        if (media == null) {
            return ResponseEntity.notFound().build();
        }

        MediaResource resource = mediaAssembler.toModel(media);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/{mediaId}/download")
    @Operation(summary = "Descargar archivo multimedia")
    @ApiResponse(responseCode = "200", description = "Archivo descargado")
    @ApiResponse(responseCode = "404", description = "Archivo no encontrado")
    public ResponseEntity<Resource> downloadMedia(
            @Parameter(description = "ID del archivo multimedia")
            @PathVariable String mediaId) {
        
        try {
            Media media = mediaQuery.findById(mediaId);
            if (media == null) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get(media.getUrl());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                           "attachment; filename=\"" + media.getOriginalFilename() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, media.getMimeType())
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "Obtener archivos multimedia de un post")
    @ApiResponse(responseCode = "200", description = "Lista de archivos del post")
    public ResponseEntity<List<MediaResource>> getPostMedia(
            @Parameter(description = "ID del post")
            @PathVariable String postId) {
        
        List<Media> mediaList = mediaQuery.findByPostId(postId);
        List<MediaResource> resources = mediaList.stream()
                .map(mediaAssembler::toModel)
                .toList();

        return ResponseEntity.ok(resources);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Obtener archivos multimedia de un usuario")
    @ApiResponse(responseCode = "200", description = "Lista paginada de archivos del usuario")
    public ResponseEntity<Page<MediaResource>> getUserMedia(
            @Parameter(description = "ID del usuario")
            @PathVariable String userId,
            @PageableDefault(size = 20) Pageable pageable) {
        
        Page<Media> mediaPage = mediaQuery.findByUserId(userId, pageable);
        Page<MediaResource> resourcePage = mediaPage.map(mediaAssembler::toModel);

        return ResponseEntity.ok(resourcePage);
    }

    @DeleteMapping("/{mediaId}")
    @Operation(summary = "Eliminar archivo multimedia")
    @ApiResponse(responseCode = "204", description = "Archivo eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Archivo no encontrado")
    public ResponseEntity<Void> deleteMedia(
            @Parameter(description = "ID del archivo multimedia")
            @PathVariable String mediaId,
            @Parameter(description = "ID del usuario (para verificación)")
            @RequestParam String userId) {
        
        boolean deleted = mediaCommand.deleteMedia(mediaId, userId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/stats/user/{userId}")
    @Operation(summary = "Obtener estadísticas de uso de storage del usuario")
    @ApiResponse(responseCode = "200", description = "Estadísticas de storage")
    public ResponseEntity<MediaValidationService.StorageMetrics> getUserStorageStats(
            @Parameter(description = "ID del usuario")
            @PathVariable String userId) {
        
        MediaValidationService.StorageMetrics metrics = 
            mediaValidationService.calculateStorageMetrics(userId);
        
        return ResponseEntity.ok(metrics);
    }

    @PostMapping("/validate")
    @Operation(summary = "Validar archivos sin subirlos")
    @ApiResponse(responseCode = "200", description = "Resultado de validación")
    public ResponseEntity<MediaValidationService.MediaCollectionValidationResult> validateFiles(
            @Parameter(description = "Archivos a validar")
            @RequestParam("files") List<MultipartFile> files) {
        
        MediaValidationService.MediaCollectionValidationResult result = 
            mediaValidationService.validateMediaCollection(files);
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/types")
    @Operation(summary = "Obtener tipos de archivos permitidos")
    @ApiResponse(responseCode = "200", description = "Información de tipos permitidos")
    public ResponseEntity<Map<String, Object>> getAllowedFileTypes() {
        Map<String, Object> typeInfo = new HashMap<>();
        
        Map<String, Object> imageTypes = new HashMap<>();
        imageTypes.put("types", List.of("image/jpeg", "image/png", "image/gif", "image/webp"));
        imageTypes.put("maxSize", "10MB");
        imageTypes.put("extensions", List.of("jpg", "jpeg", "png", "gif", "webp"));
        
        Map<String, Object> videoTypes = new HashMap<>();
        videoTypes.put("types", List.of("video/mp4", "video/webm", "video/mov", "video/avi"));
        videoTypes.put("maxSize", "100MB");
        videoTypes.put("extensions", List.of("mp4", "webm", "mov", "avi"));
        
        Map<String, Object> audioTypes = new HashMap<>();
        audioTypes.put("types", List.of("audio/mp3", "audio/wav", "audio/ogg", "audio/m4a"));
        audioTypes.put("maxSize", "50MB");
        audioTypes.put("extensions", List.of("mp3", "wav", "ogg", "m4a"));
        
        typeInfo.put("images", imageTypes);
        typeInfo.put("videos", videoTypes);
        typeInfo.put("audio", audioTypes);
        typeInfo.put("maxFilesPerPost", 10);
        
        return ResponseEntity.ok(typeInfo);
    }
}