package com.drawnet.feed_service.infrastructure.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Configuración de Feign Client para comunicación entre microservicios
 */
@Configuration
@EnableFeignClients(basePackages = "com.drawnet.feed_service.infrastructure.external.clients")
public class FeignConfig {

    /**
     * Interceptor para propagar headers de autenticación
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = 
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                
                // Propagar Authorization header
                String authorization = request.getHeader("Authorization");
                if (authorization != null) {
                    requestTemplate.header("Authorization", authorization);
                }
                
                // Propagar X-User-Id header
                String userId = request.getHeader("X-User-Id");
                if (userId != null) {
                    requestTemplate.header("X-User-Id", userId);
                }
                
                // Propagar X-Request-ID para tracing
                String requestId = request.getHeader("X-Request-ID");
                if (requestId != null) {
                    requestTemplate.header("X-Request-ID", requestId);
                }
                
                // Agregar header de servicio origen
                requestTemplate.header("X-Source-Service", "feed-service");
            }
        };
    }

    /**
     * Configuración de logging para Feign
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    /**
     * Decoder personalizado para manejar errores de Feign
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomFeignErrorDecoder();
    }
}