package com.drawnet.feed_service.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.util.unit.DataSize;

import jakarta.servlet.MultipartConfigElement;

/**
 * Configuración para manejo de archivos multimedia
 */
@Configuration
public class MediaConfig {

    @Value("${app.media.max-file-size:100MB}")
    private String maxFileSize;

    @Value("${app.media.max-request-size:500MB}")
    private String maxRequestSize;

    @Value("${app.media.upload-path:./uploads}")
    private String uploadPath;

    @Value("${app.media.temp-dir:./temp}")
    private String tempDir;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        
        factory.setMaxFileSize(DataSize.parse(maxFileSize));
        factory.setMaxRequestSize(DataSize.parse(maxRequestSize));
        factory.setLocation(tempDir);
        
        return factory.createMultipartConfig();
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    // Getters para usar en otros servicios
    public String getUploadPath() {
        return uploadPath;
    }

    public String getTempDir() {
        return tempDir;
    }

    public String getMaxFileSize() {
        return maxFileSize;
    }

    public String getMaxRequestSize() {
        return maxRequestSize;
    }
}