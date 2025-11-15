package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.PublicarIlustracionCommand;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.PublicarIlustracionResource;

public class PublicarIlustracionCommandFromResourceAssembler {
    public static PublicarIlustracionCommand toCommandFromResource(PublicarIlustracionResource resource, Long ilustradorId) {
        return new PublicarIlustracionCommand(
                ilustradorId,
                resource.titulo(),
                resource.descripcion(),
                resource.urlImagen(),
                resource.publicada()
        );
    }
}
