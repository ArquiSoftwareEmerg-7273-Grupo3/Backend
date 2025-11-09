package com.drawnet.feed_service.application.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Servicio para almacenar archivos en el sistema de archivos local
 */
@Service
@Slf4j
public class FileStorageService {

    @Value("${app.media.upload-path:./uploads/feed}")
    private String uploadPath;

    @Value("${app.media.max-file-size:100MB}")
    private String maxFileSize;

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    private static final List<String> ALLOWED_VIDEO_TYPES = Arrays.asList(
        "video/mp4", "video/webm", "video/quicktime"
    );

    /**
     * Guardar un archivo en el sistema
     * 
     * @param file Archivo a guardar
     * @return URL relativa del archivo guardado
     */
    public String storeFile(MultipartFile file) {
        // Validar que el archivo no esté vacío
        if (file.isEmpty()) {
            throw new RuntimeException("No se puede guardar un archivo vacío");
        }

        // Validar tipo de archivo
        String contentType = file.getContentType();
        if (!isAllowedFileType(contentType)) {
            throw new RuntimeException("Tipo de archivo no permitido: " + contentType);
        }

        try {
            // Obtener nombre original del archivo
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            
            // Validar nombre del archivo
            if (originalFilename.contains("..")) {
                throw new RuntimeException("Nombre de archivo inválido: " + originalFilename);
            }

            // Generar nombre único para el archivo
            String fileExtension = getFileExtension(originalFilename);
            String uniqueFilename = generateUniqueFilename(fileExtension);

            // Crear directorio si no existe
            Path uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
            Files.createDirectories(uploadDir);

            // Crear subdirectorio por fecha (año/mes/día)
            String dateSubDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path datePath = uploadDir.resolve(dateSubDir);
            Files.createDirectories(datePath);

            // Ruta completa del archivo
            Path targetLocation = datePath.resolve(uniqueFilename);

            // Copiar archivo al destino
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            log.info("✅ Archivo guardado exitosamente: {}", targetLocation);

            // Retornar URL relativa (para servir vía HTTP)
            return "/uploads/feed/" + dateSubDir + "/" + uniqueFilename;

        } catch (IOException ex) {
            log.error("❌ Error al guardar archivo: {}", ex.getMessage());
            throw new RuntimeException("Error al guardar archivo: " + ex.getMessage(), ex);
        }
    }

    /**
     * Eliminar un archivo del sistema
     * 
     * @param fileUrl URL del archivo a eliminar
     */
    public void deleteFile(String fileUrl) {
        try {
            // Extraer ruta relativa
            String relativePath = fileUrl.replace("/uploads/feed/", "");
            Path filePath = Paths.get(uploadPath).resolve(relativePath).normalize();

            // Eliminar archivo
            Files.deleteIfExists(filePath);
            log.info("🗑️ Archivo eliminado: {}", filePath);

        } catch (IOException ex) {
            log.error("❌ Error al eliminar archivo: {}", ex.getMessage());
            throw new RuntimeException("Error al eliminar archivo", ex);
        }
    }

    /**
     * Verificar si el tipo de archivo es permitido
     */
    private boolean isAllowedFileType(String contentType) {
        if (contentType == null) {
            return false;
        }
        return ALLOWED_IMAGE_TYPES.contains(contentType) || 
               ALLOWED_VIDEO_TYPES.contains(contentType);
    }

    /**
     * Obtener extensión del archivo
     */
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }

    /**
     * Generar nombre único para el archivo
     */
    private String generateUniqueFilename(String extension) {
        return UUID.randomUUID().toString() + extension;
    }

    /**
     * Obtener información del archivo
     */
    public FileInfo getFileInfo(MultipartFile file) {
        return new FileInfo(
            file.getOriginalFilename(),
            file.getContentType(),
            file.getSize(),
            getFileExtension(file.getOriginalFilename())
        );
    }

    /**
     * Clase para información del archivo
     */
    public record FileInfo(
        String originalFilename,
        String contentType,
        long size,
        String extension
    ) {}
}
