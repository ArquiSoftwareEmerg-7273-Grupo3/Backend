package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform;


import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CrearChatCommand;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.CrearChatResource;

public class CrearChatCommandFromResourceAssembler {
    public static CrearChatCommand toCommandFromResource (CrearChatResource resource) {
        return new CrearChatCommand(resource.usuario1id(), resource.usuario2id());
    }
}
