package com.drawnet.artcollab.CollaborativeProjects.domain.model.queries;

public record GetPostulacionesByProyectoIdQuery(Long proyectoId) {
    public GetPostulacionesByProyectoIdQuery {
        if (proyectoId == null) {
            throw new IllegalArgumentException("El ID del proyecto no puede ser nulo");
        }
    }
}
