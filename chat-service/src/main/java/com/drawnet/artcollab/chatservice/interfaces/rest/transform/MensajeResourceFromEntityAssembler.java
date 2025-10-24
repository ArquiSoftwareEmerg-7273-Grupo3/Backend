package com.drawnet.artcollab.chatservice.interfaces.rest.transform;

import com.drawnet.artcollab.chatservice.domain.model.aggregates.Mensaje;
import com.drawnet.artcollab.chatservice.interfaces.rest.resources.MensajeResource;

public class MensajeResourceFromEntityAssembler {
    public static MensajeResource toResourceFromEntity(Mensaje mensaje) {
        return new MensajeResource(
                mensaje.getId(),
                mensaje.getChat().getId(),
                mensaje.getRemitenteId(),
                mensaje.getTexto()
        );
    }
}
