package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.entities.Postulacion;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.PostulacionResource;

public class PostulacionResourceFromEntityAssembler {

    public static PostulacionResource toResourceFromEntity(Postulacion entity) {
        return new PostulacionResource(
                entity.getId(),
                entity.getProyectoId(),
                entity.getIlustradorId(),
                entity.getEstado(),
                entity.getFecha()
        );
    }
}
