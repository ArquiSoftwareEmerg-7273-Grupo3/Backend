package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.entities.Postulacion;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.PostulacionResource;

public class PostulacionResourceFromEntityAssembler {

    public static PostulacionResource toResourceFromEntity(Postulacion entity) {
        LocalDateTime fechaPostulacion = entity.getFechaPostulacion();

        return new PostulacionResource(
                entity.getId(),
                entity.getProyectoId(),
                entity.getIlustradorId(),
                entity.getEstado(),
                fechaPostulacion
        );
    }

 
}
