package com.drawnet.artcollab.portafolioservice.interfaces.rest.resources;

import java.util.List;

public record CategoriaResource(
        Long id,
        String nombre,
        String descripcion,
        Integer orden,
        List<IlustracionResource> ilustraciones
) {
}
