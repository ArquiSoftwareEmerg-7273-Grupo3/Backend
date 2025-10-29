package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources;

import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.IlustracionResource;

import java.util.List;

public record PortafolioResource(
        Long id,
        String titulo,
        String descripcion,
        String urlImagen,
        List<IlustracionResource> ilustraciones
) {
}
