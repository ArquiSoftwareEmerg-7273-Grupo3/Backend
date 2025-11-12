package com.drawnet.artcollab.portafolioservice.interfaces.rest.transform;

import com.drawnet.artcollab.portafolioservice.domain.model.commands.CalificarIlustracionCommand;
import com.drawnet.artcollab.portafolioservice.domain.model.entities.Ilustracion;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.CalificarIlustracionResource;

public class CalificarIlustracionCommandFromResourceAssembler {
    public static CalificarIlustracionCommand toCommandFromResource(CalificarIlustracionResource resource, Long ilustracionId, Long usuarioId) {
        return new CalificarIlustracionCommand(
                ilustracionId,
                usuarioId,
                resource.puntuacion(),
                resource.comentario()
        );

    }
}

