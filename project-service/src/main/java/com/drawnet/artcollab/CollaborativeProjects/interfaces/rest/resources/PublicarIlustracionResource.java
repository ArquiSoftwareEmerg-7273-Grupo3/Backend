package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources;

public record PublicarIlustracionResource(
        String titulo,
        String descripcion,
        String urlImagen,
        boolean publicada
) {
}
