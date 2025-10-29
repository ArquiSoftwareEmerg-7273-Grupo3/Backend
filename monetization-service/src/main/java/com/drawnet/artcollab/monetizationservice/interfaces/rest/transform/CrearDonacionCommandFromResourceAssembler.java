package com.drawnet.artcollab.monetizationservice.interfaces.rest.transform;

import com.drawnet.artcollab.monetizationservice.domain.model.commands.CrearDonacionCommand;
import com.drawnet.artcollab.monetizationservice.interfaces.rest.resources.CrearDonacionResource;

public class CrearDonacionCommandFromResourceAssembler {
    public static CrearDonacionCommand toCommandFromResource(CrearDonacionResource resource) {
        return new CrearDonacionCommand(
                resource.donanteId(),
                resource.receptorId(),
                resource.monto()
        );
    }
}
