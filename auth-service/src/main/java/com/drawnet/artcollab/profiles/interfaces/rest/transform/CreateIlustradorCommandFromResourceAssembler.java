package com.drawnet.artcollab.profiles.interfaces.rest.transform;


import com.drawnet.artcollab.profiles.domain.model.commands.CreateIlustradorCommand;
import com.drawnet.artcollab.profiles.interfaces.rest.resources.CreateEscritorResource;
import com.drawnet.artcollab.profiles.interfaces.rest.resources.CreateIlustradorResource;

public class CreateIlustradorCommandFromResourceAssembler {
    public static CreateIlustradorCommand toCommandFromResource(CreateIlustradorResource resource) {
        return new CreateIlustradorCommand(
                resource.firstName(),
                resource.lastName(),
                resource.biografia(),
                resource.foto(),
                resource.redes(),
                resource.suscripcion(),
                resource.userId()
        );
    }
}
