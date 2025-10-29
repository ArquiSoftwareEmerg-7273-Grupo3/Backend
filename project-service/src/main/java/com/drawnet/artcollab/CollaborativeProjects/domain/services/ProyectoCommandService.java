package com.drawnet.artcollab.CollaborativeProjects.domain.services;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Proyecto;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CreateProyectoCommand;

import java.util.Optional;

public interface ProyectoCommandService {

    Optional<Proyecto> handle(CreateProyectoCommand command);
}
