package com.drawnet.artcolab.libroservice.interfaces.rest.transform;

import com.drawnet.artcolab.libroservice.domain.model.commands.CrearLibroCommand;
import com.drawnet.artcolab.libroservice.interfaces.rest.resources.CrearLibroResource;

public class CrearLibroCommandFromResourceAssembler {

    public static CrearLibroCommand toCommandFromResource(CrearLibroResource resource, Long escritorId) {
        return new CrearLibroCommand(
                escritorId,
                resource.titulo(),
                resource.sinopsis(),
                resource.urlImagen(),
                resource.urlLibro()
        );
    }
}
