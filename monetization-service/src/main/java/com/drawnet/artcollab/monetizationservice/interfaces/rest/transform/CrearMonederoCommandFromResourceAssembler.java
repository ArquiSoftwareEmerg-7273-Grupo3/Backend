package com.drawnet.artcollab.monetizationservice.interfaces.rest.transform;

import com.drawnet.artcollab.monetizationservice.domain.model.commands.CrearMonederoCommand;

public class CrearMonederoCommandFromResourceAssembler {
    public static CrearMonederoCommand toCommandFromResource(Long usuarioId) {
        return new CrearMonederoCommand(usuarioId);
    }
}