package com.drawnet.artcollab.portafolioservice.interfaces.rest.resources;

public record CrearCategoriaResource(
        String nombre,
        String descripcion,
        Integer orden
) {
}
