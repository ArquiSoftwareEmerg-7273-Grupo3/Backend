package com.drawnet.artcollab.profiles.interfaces.rest.transform;


import com.drawnet.artcollab.profiles.domain.model.aggregates.Ilustrador;
import com.drawnet.artcollab.profiles.interfaces.rest.resources.IlustradorResource;

public class IlustradorResourceFromEntityAssembler {
    public static IlustradorResource toResourceFromEntity(Ilustrador entity) {
        var user = entity.getUser();
        return new IlustradorResource(
                entity.getId(),
                entity.getNombreArtistico(),
                entity.getSubscripcion(),
                user != null ? user.getId() : null,
                user != null ? user.getUsername() : null,
                user != null ? user.getNombres() : null,
                user != null ? user.getApellidos() : null
        );
    }
}
