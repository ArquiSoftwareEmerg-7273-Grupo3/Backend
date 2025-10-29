package com.drawnet.artcolab.libroservice.interfaces.rest.transform;

import com.drawnet.artcolab.libroservice.domain.model.commands.CalificarLibroCommand;
import com.drawnet.artcolab.libroservice.interfaces.rest.resources.CalificarLibroResource;

public class CalificarLibroCommandFromResourceAssembler {

    public static CalificarLibroCommand toCommandFromResource(CalificarLibroResource resource, Long libroId) {
        return new CalificarLibroCommand(
                libroId,
                resource.usuarioId(),
                resource.puntuacion(),
                resource.comentario()
        );
    }

}
