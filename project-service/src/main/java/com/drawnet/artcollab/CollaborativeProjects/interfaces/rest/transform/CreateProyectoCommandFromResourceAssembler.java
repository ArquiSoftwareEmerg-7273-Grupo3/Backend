package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CreateProyectoCommand;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.CreateProyectoResource;

import java.time.LocalDateTime;

public class CreateProyectoCommandFromResourceAssembler {
    public static CreateProyectoCommand toCommandFromResource(CreateProyectoResource resource, Long escritorId) {
    // Resource now exposes LocalDateTime directly
    LocalDateTime fechaInicio = resource.fechaInicio();
    LocalDateTime fechaFin = resource.fechaFin();

        return new CreateProyectoCommand(
                escritorId,
                resource.titulo(),
                resource.descripcion(),
                resource.presupuesto(),
                fechaInicio,
                fechaFin,
                resource.maxPostulaciones()
        );
    }

    // no conversion needed: resource uses LocalDateTime
}
