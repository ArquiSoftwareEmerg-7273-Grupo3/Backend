package com.drawnet.artcollab.CollaborativeProjects.domain.model.commands;

public record CalificarIlustracionCommand(
        Long ilustracionId,
        Long usuarioId,
        Integer puntuacion,
        String comentario
) {
}
