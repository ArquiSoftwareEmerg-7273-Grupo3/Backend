package com.drawnet.artcollab.profiles.interfaces.rest.transform;


import com.drawnet.artcollab.profiles.domain.model.commands.CreateEscritorCommand;
import com.drawnet.artcollab.profiles.interfaces.rest.resources.CreateEscritorResource;

public class CreateEscritorCommandFromResourceAssembler {
    public static CreateEscritorCommand toCommandFromResource(CreateEscritorResource resource) {
        return new CreateEscritorCommand(
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
