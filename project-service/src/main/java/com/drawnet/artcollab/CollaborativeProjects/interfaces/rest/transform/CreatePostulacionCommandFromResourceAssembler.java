package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CreatePostulacionCommand;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.CreatePostulacionResource;

public class CreatePostulacionCommandFromResourceAssembler {
    public static CreatePostulacionCommand toCommandFromResource(Long proyectoId, Long ilustradorId, CreatePostulacionResource resource) {
        return new CreatePostulacionCommand(
                proyectoId,
                ilustradorId,
                "En espera de revisión",
                "EN_ESPERA",
                resource.fecha()
        );
    }
}
