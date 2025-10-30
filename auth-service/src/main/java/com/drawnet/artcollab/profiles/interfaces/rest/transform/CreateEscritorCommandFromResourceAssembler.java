package com.drawnet.artcollab.profiles.interfaces.rest.transform;


import com.drawnet.artcollab.profiles.domain.model.commands.CreateEscritorCommand;
import com.drawnet.artcollab.profiles.interfaces.rest.resources.CreateEscritorResource;

public class CreateEscritorCommandFromResourceAssembler {
    public static CreateEscritorCommand toCommandFromResource(CreateEscritorResource resource, Long userId) {
        return new CreateEscritorCommand(
                resource.razonSocial(),
                resource.ruc(),
                resource.nombreComercial(),
                resource.sitioWeb(),
                resource.logo(),
                resource.ubicacionEmpresa(),
                resource.tipoEmpresa(),
                userId // Se obtiene del token JWT
        );
    }
}
