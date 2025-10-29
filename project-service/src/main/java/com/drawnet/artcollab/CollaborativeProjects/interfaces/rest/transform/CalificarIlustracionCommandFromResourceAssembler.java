package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CalificarIlustracionCommand;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.CalificarIlustracionResource;

public class CalificarIlustracionCommandFromResourceAssembler {
    public static CalificarIlustracionCommand toCommandFromResource(CalificarIlustracionResource resource, Long ilustracionId) {
        return new CalificarIlustracionCommand(
                ilustracionId,
                resource.usuarioId(),
                resource.puntuacion(),
                resource.comentario()
        );

    }
}

