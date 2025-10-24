package com.drawnet.artcollab.portafolioservice.interfaces.rest.resources;

public record CalificacionResource(
        int puntuacion,
        String comentario,
        String fecha
) {
}
