package com.drawnet.artcollab.CollaborativeProjects.domain.services;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CreatePostulacionCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.entities.Postulacion;

import java.util.Optional;

public interface PostulacionCommandService {

    Optional<Postulacion> handle(CreatePostulacionCommand command);

    Optional<Postulacion> actualizarEstado(Long id, String nuevoEstado); // Nuevo método

}
