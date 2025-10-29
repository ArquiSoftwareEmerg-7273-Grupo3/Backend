package com.drawnet.artcollab.portafolioservice.interfaces.rest.resources;

public record AgregarIlustracionAPortafolioResource(
        Long ilustracionId,
        String titulo,
        String descripcion,
        String urlImagen
) {
}
