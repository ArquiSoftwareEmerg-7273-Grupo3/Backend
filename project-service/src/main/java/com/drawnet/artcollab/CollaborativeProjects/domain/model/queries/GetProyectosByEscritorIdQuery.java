package com.drawnet.artcollab.CollaborativeProjects.domain.model.queries;

public record GetProyectosByEscritorIdQuery(Long escritorId) {
    public GetProyectosByEscritorIdQuery {
        if (escritorId == null) {
            throw new IllegalArgumentException("El ID del escritor no puede ser nulo");
        }
    }
}
