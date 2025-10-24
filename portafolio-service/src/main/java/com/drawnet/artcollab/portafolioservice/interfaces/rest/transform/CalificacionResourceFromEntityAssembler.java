package com.drawnet.artcollab.portafolioservice.interfaces.rest.transform;

import com.drawnet.artcollab.portafolioservice.domain.model.valueobjects.Calificacion;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.CalificacionResource;

public class CalificacionResourceFromEntityAssembler {
    public static CalificacionResource toResource(Calificacion calificacion) {
        return new CalificacionResource(
                calificacion.getPuntuacion(),
                calificacion.getComentario(),
                calificacion.getFecha().toString()
        );
    }
}
