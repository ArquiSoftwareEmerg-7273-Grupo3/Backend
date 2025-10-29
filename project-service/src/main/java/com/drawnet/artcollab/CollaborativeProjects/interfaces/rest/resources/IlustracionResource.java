package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources;

public record IlustracionResource(
        Long id,
        String titulo,
        String descripcion,
        String urlImagen
) {
}
