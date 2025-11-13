package com.drawnet.artcollab.CollaborativeProjects.domain.services;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Postulacion;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.AprobarPostulacionCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CancelarPostulacionCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CreatePostulacionCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.RechazarPostulacionCommand;

import java.util.Optional;

public interface PostulacionCommandService {

    Optional<Postulacion> handle(CreatePostulacionCommand command);
    
    Optional<Postulacion> handle(AprobarPostulacionCommand command);
    
    Optional<Postulacion> handle(RechazarPostulacionCommand command);
    
    Optional<Postulacion> handle(CancelarPostulacionCommand command);

}
