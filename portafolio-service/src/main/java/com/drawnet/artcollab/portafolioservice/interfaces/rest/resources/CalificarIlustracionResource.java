package com.drawnet.artcollab.portafolioservice.interfaces.rest.resources;

public record CalificarIlustracionResource(
        Long usuarioId,
        int puntuacion,
        String comentario
) {
}
