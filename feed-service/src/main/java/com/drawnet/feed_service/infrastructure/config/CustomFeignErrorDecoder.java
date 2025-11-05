package com.drawnet.feed_service.infrastructure.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Decoder personalizado para manejar errores específicos de Feign
 */
public class CustomFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        
        switch (status) {
            case NOT_FOUND:
                if (methodKey.contains("getUserProfile")) {
                    return new UserNotFoundException("Usuario no encontrado");
                } else if (methodKey.contains("getArtwork")) {
                    return new ArtworkNotFoundException("Obra no encontrada");
                }
                break;
                
            case FORBIDDEN:
                return new ServiceAccessDeniedException("Acceso denegado al servicio");
                
            case SERVICE_UNAVAILABLE:
                return new ServiceUnavailableException("Servicio temporalmente no disponible");
                
            case TOO_MANY_REQUESTS:
                return new RateLimitExceededException("Límite de requests excedido");
        }
        
        return defaultErrorDecoder.decode(methodKey, response);
    }

    // Excepciones personalizadas
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class ArtworkNotFoundException extends RuntimeException {
        public ArtworkNotFoundException(String message) {
            super(message);
        }
    }

    public static class ServiceAccessDeniedException extends RuntimeException {
        public ServiceAccessDeniedException(String message) {
            super(message);
        }
    }

    public static class ServiceUnavailableException extends RuntimeException {
        public ServiceUnavailableException(String message) {
            super(message);
        }
    }

    public static class RateLimitExceededException extends RuntimeException {
        public RateLimitExceededException(String message) {
            super(message);
        }
    }
}