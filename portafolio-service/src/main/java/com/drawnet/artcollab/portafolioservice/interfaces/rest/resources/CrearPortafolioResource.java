package com.drawnet.artcollab.portafolioservice.interfaces.rest.resources;

public record CrearPortafolioResource(
        String titulo,
        String descripcion,
        String urlImagen
) {
}
