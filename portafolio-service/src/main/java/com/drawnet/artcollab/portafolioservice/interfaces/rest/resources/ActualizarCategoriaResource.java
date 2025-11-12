package com.drawnet.artcollab.portafolioservice.interfaces.rest.resources;

public record ActualizarCategoriaResource(
        String nombre,
        String descripcion,
        Integer orden
) {
}
