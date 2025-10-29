package com.drawnet.artcollab.CollaborativeProjects.domain.model.queries;

public record GetPostulacionesByIlustradorIdQuery(Long ilustradorId) {
    public GetPostulacionesByIlustradorIdQuery {
        if (ilustradorId == null) {
            throw new IllegalArgumentException("El ID del ilustrador no puede ser nulo");
        }
    }
}
