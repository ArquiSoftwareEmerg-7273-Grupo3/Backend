package com.drawnet.artcollab.iam.interfaces.rest.transform;


import com.drawnet.artcollab.iam.domain.model.commands.SignUpCommand;
import com.drawnet.artcollab.iam.domain.model.entities.Role;
import com.drawnet.artcollab.iam.domain.model.valueobjects.Roles;
import com.drawnet.artcollab.iam.interfaces.rest.resources.SignUpResource;

public class SignUpCommandFromResourceAssembler {
    public static SignUpCommand toCommandFromResource(SignUpResource resource) {
        //var erole = new Role(Roles.valueOf(resource.role()));
        return new SignUpCommand(
                resource.username(),
                resource.password(),
                resource.ubicacion(),
                resource.nombres(),
                resource.apellidos(),
                resource.telefono(),
                resource.foto(),
                resource.descripcion(),
                resource.fechaNacimiento(),
                resource.redesSociales()
                );
    }
}
