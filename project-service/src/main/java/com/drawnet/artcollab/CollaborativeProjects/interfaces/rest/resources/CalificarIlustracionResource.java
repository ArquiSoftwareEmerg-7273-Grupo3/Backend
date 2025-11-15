package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources;

public record CalificarIlustracionResource(
        Long usuarioId,
        int puntuacion,
        String comentario
) {
}
