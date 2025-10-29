package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources;

public record AgregarIlustracionAPortafolioResource(
        Long ilustracionId,
        String titulo,
        String descripcion,
        String urlImagen
) {
}
