package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CrearPortafolioCommand;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.CrearPortafolioResource;

public class CrearPortafolioCommandFromResourceAssembler {
    public static CrearPortafolioCommand toCommandFromResource(CrearPortafolioResource resource, Long ilustradorId) {
        return new CrearPortafolioCommand(
                ilustradorId,
                resource.titulo(),
                resource.descripcion(),
                resource.urlImagen()
        );
    }
}
