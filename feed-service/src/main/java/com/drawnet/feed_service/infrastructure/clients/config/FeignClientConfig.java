package com.drawnet.feed_service.infrastructure.clients.config;

import feign.Logger;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración para Feign Clients
 * Incluye logging y manejo de errores
 */
@Configuration
@Slf4j
public class FeignClientConfig {
    
    /**
     * Nivel de logging para Feign
     * FULL: Registra headers, body y metadata de requests y responses
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }
    
    /**
     * Decodificador de errores personalizado
     * Permite manejar errores de forma específica sin lanzar excepciones
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            log.error("Error calling {}: Status {} - {}", 
                methodKey, 
                response.status(), 
                response.reason());
            
            // Retornar la excepción por defecto
            return new ErrorDecoder.Default().decode(methodKey, response);
        };
    }
}
