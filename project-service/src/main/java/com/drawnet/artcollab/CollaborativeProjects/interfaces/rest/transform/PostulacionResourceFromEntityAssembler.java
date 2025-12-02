package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Postulacion;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.PostulacionResource;

public class PostulacionResourceFromEntityAssembler {

    public static PostulacionResource toResourceFromEntity(Postulacion entity) {
        return new PostulacionResource(
                entity.getId(),
                entity.getProyectoId(),
                entity.getIlustradorId(),
                entity.getEstado(),
                entity.getFechaPostulacion(),
                entity.getMensaje(),
                entity.getRespuesta(),
                entity.getFechaRespuesta()
        );
    }

 
}
