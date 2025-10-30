package com.drawnet.artcollab.profiles.interfaces.rest.transform;


import com.drawnet.artcollab.profiles.domain.model.commands.CreateIlustradorCommand;
import com.drawnet.artcollab.profiles.interfaces.rest.resources.CreateIlustradorResource;

public class CreateIlustradorCommandFromResourceAssembler {
    public static CreateIlustradorCommand toCommandFromResource(CreateIlustradorResource resource, Long userId) {
        return new CreateIlustradorCommand(
                resource.nombreArtistico(),
                userId // Se obtiene del token JWT
        );
    }
}
