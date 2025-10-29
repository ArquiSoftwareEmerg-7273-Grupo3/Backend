package com.drawnet.artcollab.monetizationservice.interfaces.rest.transform;

import com.drawnet.artcollab.monetizationservice.domain.model.commands.CrearSuscripcionCommand;
import com.drawnet.artcollab.monetizationservice.interfaces.rest.resources.CrearSuscripcionResource;

public class CrearSuscripcionCommandFromResourceAssembler {
    public static CrearSuscripcionCommand toCommandFromResource(CrearSuscripcionResource resource) {
        return new CrearSuscripcionCommand(
                resource.usuarioId()
        );
    }
}
