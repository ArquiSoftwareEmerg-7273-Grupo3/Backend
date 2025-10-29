package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.valueobjects.Calificacion;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.CalificacionResource;

public class CalificacionResourceFromEntityAssembler {
    public static CalificacionResource toResource(Calificacion calificacion) {
        return new CalificacionResource(
                calificacion.getPuntuacion(),
                calificacion.getComentario(),
                calificacion.getFecha().toString()
        );
    }
}
