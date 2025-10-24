package com.drawnet.artcolab.libroservice.interfaces.rest.transform;

import com.drawnet.artcolab.libroservice.domain.model.valueobjects.Calificacion;
import com.drawnet.artcolab.libroservice.interfaces.rest.resources.CalificacionResource;

public class CalificacionResourceFromEntityAssembler {
    public static CalificacionResource toResourceFromEntity(Calificacion calificacion) {
        return new CalificacionResource(
                calificacion.getPuntuacion(),
                calificacion.getComentario(),
                calificacion.getFecha().toString()
        );
    }
}
