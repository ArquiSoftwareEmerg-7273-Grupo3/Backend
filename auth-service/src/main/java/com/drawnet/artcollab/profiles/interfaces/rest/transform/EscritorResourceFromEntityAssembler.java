package com.drawnet.artcollab.profiles.interfaces.rest.transform;


import com.drawnet.artcollab.profiles.domain.model.aggregates.Escritor;
import com.drawnet.artcollab.profiles.interfaces.rest.resources.EscritorResource;

public class EscritorResourceFromEntityAssembler {
    public static EscritorResource toResourceFromEntity(Escritor entity) {
        var user = entity.getUser();
        return new EscritorResource(
                entity.getId(),
                entity.getRazonSocial(),
                entity.getRuc(),
                entity.getNombreComercial(),
                entity.getSitioWeb(),
                entity.getLogo(),
                entity.getUbicacionEmpresa(),
                entity.getTipoEmpresa(),
                user != null ? user.getId() : null,
                user != null ? user.getUsername() : null,
                user != null ? user.getNombres() : null,
                user != null ? user.getApellidos() : null
        );
    }
}
