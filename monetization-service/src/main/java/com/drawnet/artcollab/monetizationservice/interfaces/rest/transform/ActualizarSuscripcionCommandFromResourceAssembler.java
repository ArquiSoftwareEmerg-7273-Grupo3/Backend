package com.drawnet.artcollab.monetizationservice.interfaces.rest.transform;

import com.drawnet.artcollab.monetizationservice.domain.model.commands.ActualizarSuscripcionCommand;

public class ActualizarSuscripcionCommandFromResourceAssembler {
    public static ActualizarSuscripcionCommand toCommandFromResource(Long usuarioId) {
        return new ActualizarSuscripcionCommand(usuarioId);
    }
}