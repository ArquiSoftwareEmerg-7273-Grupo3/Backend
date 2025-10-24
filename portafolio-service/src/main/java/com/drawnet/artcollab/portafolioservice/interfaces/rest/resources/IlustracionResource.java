package com.drawnet.artcollab.portafolioservice.interfaces.rest.resources;

public record IlustracionResource(
        Long id,
        String titulo,
        String descripcion,
        String urlImagen
) {
}
