package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform;


import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.EnviarMensajeCommand;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.EnviarMensajeResource;

public class EnviarMensajeCommandFromResourceAssembler {
    public static EnviarMensajeCommand toCommandFromResource(Long chatId, EnviarMensajeResource resource) {
        return new EnviarMensajeCommand(chatId, resource.remitenteId(), resource.texto());
    }
}
