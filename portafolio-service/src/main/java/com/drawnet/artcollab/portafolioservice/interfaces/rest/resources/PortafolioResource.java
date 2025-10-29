package com.drawnet.artcollab.portafolioservice.interfaces.rest.resources;

import java.util.List;

public record PortafolioResource(
        Long id,
        String titulo,
        String descripcion,
        String urlImagen,
        List<IlustracionResource> ilustraciones
) {
}
