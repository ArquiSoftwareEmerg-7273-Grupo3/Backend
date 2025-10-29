package com.drawnet.artcollab.portafolioservice.domain.model.commands;

public record CalificarIlustracionCommand(
        Long ilustracionId,
        Long usuarioId,
        Integer puntuacion,
        String comentario
) {
}
