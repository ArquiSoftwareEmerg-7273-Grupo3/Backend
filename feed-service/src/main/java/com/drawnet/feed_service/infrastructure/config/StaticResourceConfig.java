package com.drawnet.feed_service.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuración para servir archivos estáticos (imágenes, videos)
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${app.media.upload-path:./uploads/feed}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Obtener ruta absoluta
        Path uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
        String uploadDirPath = "file:" + uploadDir.toString() + "/";

        // Configurar para servir archivos desde /uploads/feed/
        registry.addResourceHandler("/uploads/feed/**")
                .addResourceLocations(uploadDirPath);

        System.out.println("✅ Archivos estáticos configurados en: " + uploadDirPath);
        System.out.println("📂 URL de acceso: http://localhost:8083/uploads/feed/");
    }
}
