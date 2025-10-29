package com.drawnet.artcollab.profiles.interfaces.rest.transform;


import com.drawnet.artcollab.profiles.domain.model.aggregates.Escritor;
import com.drawnet.artcollab.profiles.interfaces.rest.resources.EscritorResource;

public class EscritorResourceFromEntityAssembler {
    public static EscritorResource toResourceFromEntity(Escritor entity) {
        return new EscritorResource(
                entity.getId(),
                entity.getFullName(),
                entity.getBiografia(),
                entity.getFoto(),
                entity.getRedes(),
                entity.getSuscripcion()
        );
    }
}
