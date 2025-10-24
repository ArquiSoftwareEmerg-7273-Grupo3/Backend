package com.drawnet.artcollab.chatservice.interfaces.rest.transform;

import com.drawnet.artcollab.chatservice.domain.model.aggregates.Chat;
import com.drawnet.artcollab.chatservice.domain.model.aggregates.Mensaje;
import com.drawnet.artcollab.chatservice.interfaces.rest.resources.MensajeConReceptorResource;

public class MensajeConReceptorResourceFromEntityAssembler {

    public static MensajeConReceptorResource toResourceFromEntity(Mensaje mensaje) {
        Chat chat = mensaje.getChat();
        Long remitenteId = mensaje.getRemitenteId();

        Long receptorId = chat.getUsuario1Id().equals(remitenteId)
                ? chat.getUsuario2Id()
                : chat.getUsuario1Id();

        return new MensajeConReceptorResource(receptorId, mensaje.getTexto());
    }

}
