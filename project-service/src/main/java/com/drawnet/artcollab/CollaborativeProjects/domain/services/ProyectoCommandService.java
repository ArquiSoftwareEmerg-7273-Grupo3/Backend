package com.drawnet.artcollab.CollaborativeProjects.domain.services;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Proyecto;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CerrarProyectoCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CreateProyectoCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.FinalizarProyectoCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.UpdateProyectoCommand;

import java.util.Optional;

public interface ProyectoCommandService {

    Optional<Proyecto> handle(CreateProyectoCommand command);
    
    Optional<Proyecto> handle(UpdateProyectoCommand command);
    
    Optional<Proyecto> handle(CerrarProyectoCommand command);
    
    Optional<Proyecto> handle(FinalizarProyectoCommand command);
}
