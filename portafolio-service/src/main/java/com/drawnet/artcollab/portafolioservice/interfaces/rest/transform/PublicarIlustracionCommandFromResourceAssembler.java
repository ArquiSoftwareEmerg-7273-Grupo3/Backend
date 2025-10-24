package com.drawnet.artcollab.portafolioservice.interfaces.rest.transform;

import com.drawnet.artcollab.portafolioservice.domain.model.commands.PublicarIlustracionCommand;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.PublicarIlustracionResource;

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
