package com.drawnet.artcollab.monetizationservice.interfaces.rest.transform;

import com.drawnet.artcollab.monetizationservice.domain.model.commands.ActualizarMonederoCommand;
import com.drawnet.artcollab.monetizationservice.interfaces.rest.resources.ActualizarMonederoResource;

public class ActualizarMonederoCommandFromResourceAssembler {
    public static ActualizarMonederoCommand toCommandFromResource(Long usuarioId, ActualizarMonederoResource resource) {
        return new ActualizarMonederoCommand(usuarioId, resource.monto());
    }
}
