package com.drawnet.artcollab.portafolioservice.interfaces.rest.transform;

import com.drawnet.artcollab.portafolioservice.domain.model.commands.ActualizarCategoriaCommand;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.ActualizarCategoriaResource;

public class ActualizarCategoriaCommandFromResourceAssembler {
    public static ActualizarCategoriaCommand toCommandFromResource(ActualizarCategoriaResource resource) {
        return new ActualizarCategoriaCommand(
                resource.nombre(),
                resource.descripcion(),
                resource.orden()
        );
    }
}
