package com.drawnet.artcollab.portafolioservice.interfaces.rest.transform;

import com.drawnet.artcollab.portafolioservice.domain.model.commands.AgregarIlustracionACategoriaCommand;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.AgregarIlustracionACategoriaResource;

public class AgregarIlustracionACategoriaCommandFromResourceAssembler {
    public static AgregarIlustracionACategoriaCommand toCommandFromResource(
            AgregarIlustracionACategoriaResource resource,
            Long categoriaId,
            Long ilustradorId) {
        return new AgregarIlustracionACategoriaCommand(
                categoriaId,
                ilustradorId,
                resource.ilustracionId(),
                resource.titulo(),
                resource.descripcion(),
                resource.urlImagen(),
                resource.publicada()
        );
    }
}
