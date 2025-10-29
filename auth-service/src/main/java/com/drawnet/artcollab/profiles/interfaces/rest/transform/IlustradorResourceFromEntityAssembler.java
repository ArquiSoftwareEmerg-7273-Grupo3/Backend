package com.drawnet.artcollab.profiles.interfaces.rest.transform;


import com.drawnet.artcollab.profiles.domain.model.aggregates.Ilustrador;
import com.drawnet.artcollab.profiles.interfaces.rest.resources.IlustradorResource;

public class IlustradorResourceFromEntityAssembler {
    public static IlustradorResource toResourceFromEntity(Ilustrador entity) {
        return new IlustradorResource(
                entity.getId(),
                entity.getFullName(),
                entity.getBiografia(),
                entity.getFoto(),
                entity.getRedes(),
                entity.getSuscripcion()
        );
    }
}
