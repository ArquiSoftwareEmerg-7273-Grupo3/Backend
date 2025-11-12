package com.drawnet.artcollab.portafolioservice.interfaces.rest.transform;

import com.drawnet.artcollab.portafolioservice.domain.model.commands.CrearCategoriaCommand;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.CrearCategoriaResource;

public class CrearCategoriaCommandFromResourceAssembler {
    public static CrearCategoriaCommand toCommandFromResource(CrearCategoriaResource resource, Long portafolioId) {
        return new CrearCategoriaCommand(
                portafolioId,
                resource.nombre(),
                resource.descripcion(),
                resource.orden()
        );
    }
}
