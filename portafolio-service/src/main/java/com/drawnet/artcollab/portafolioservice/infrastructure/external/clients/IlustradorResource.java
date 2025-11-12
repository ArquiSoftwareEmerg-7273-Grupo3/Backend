package com.drawnet.artcollab.portafolioservice.infrastructure.external.clients;

/**
 * DTO para el recurso Ilustrador del auth-service.
 * Representa el perfil del ilustrador con su relación al User.
 */
public record IlustradorResource(
    Long id,                // ilustradorId (ID de la tabla ilustrador)
    String nombreArtistico,
    Boolean subscripcion,
    Long userId,            // userId (FK a tabla users)
    String username,
    String nombres,
    String apellidos
) {}
