package com.drawnet.artcolab.libroservice.interfaces.rest.resources;

public record CalificarLibroResource(
        Long usuarioId,
        int puntuacion,
        String comentario
) {
}
