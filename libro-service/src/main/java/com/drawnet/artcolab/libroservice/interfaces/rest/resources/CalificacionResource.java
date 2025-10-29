package com.drawnet.artcolab.libroservice.interfaces.rest.resources;

import java.util.Date;

public record CalificacionResource(
        int puntuacion,
        String comentario,
        String fecha
) {
}
