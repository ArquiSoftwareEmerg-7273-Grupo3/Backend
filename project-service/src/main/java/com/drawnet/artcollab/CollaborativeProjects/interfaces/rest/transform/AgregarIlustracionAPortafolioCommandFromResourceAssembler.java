package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.AgregarIlustracionAPortafolioCommand;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.AgregarIlustracionAPortafolioResource;

public class AgregarIlustracionAPortafolioCommandFromResourceAssembler {
    public static AgregarIlustracionAPortafolioCommand toCommandFromResource(AgregarIlustracionAPortafolioResource resource, Long portafolioId, Long ilustradorId) {
        return new AgregarIlustracionAPortafolioCommand(
                portafolioId,
                resource.ilustracionId(),
                ilustradorId,
                resource.titulo(),
                resource.descripcion(),
                resource.urlImagen()
        );

    }
}
