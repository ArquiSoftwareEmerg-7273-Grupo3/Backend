package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CreateProyectoCommand;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.CreateProyectoResource;

public class CreateProyectoCommandFromResourceAssembler {
    public static CreateProyectoCommand toCommandFromResource(CreateProyectoResource resource, Long escritorId) {
        return new CreateProyectoCommand(
                escritorId,
                resource.titulo(),
                resource.descripcion(),
                resource.urlImagen(),
                resource.fecha()
        );
    }
}
