package com.drawnet.artcollab.iam.interfaces.rest.transform;


import com.drawnet.artcollab.iam.domain.model.aggregates.User;
import com.drawnet.artcollab.iam.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User entity) {
        // Mapear información del ilustrador si existe
        UserResource.IlustradorProfileResource ilustradorProfile = null;
        if (entity.getIlustrador() != null) {
            var ilustrador = entity.getIlustrador();
            ilustradorProfile = new UserResource.IlustradorProfileResource(
                ilustrador.getId(),
                ilustrador.getNombreArtistico(),
                ilustrador.getSubscripcion()
            );
        }
        
        // Mapear información del escritor si existe
        UserResource.EscritorProfileResource escritorProfile = null;
        if (entity.getEscritor() != null) {
            var escritor = entity.getEscritor();
            escritorProfile = new UserResource.EscritorProfileResource(
                escritor.getId(),
                escritor.getRazonSocial(),
                escritor.getRuc(),
                escritor.getNombreComercial(),
                escritor.getSitioWeb(),
                escritor.getLogo(),
                escritor.getUbicacionEmpresa(),
                escritor.getTipoEmpresa()
            );
        }
        
        return new UserResource(
            entity.getId(),
            entity.getUsername(),
            entity.getUbicacion(),
            entity.getNombres(),
            entity.getApellidos(),
            entity.getTelefono(),
            entity.getFoto(),
            entity.getDescripcion(),
            entity.getFechaNacimiento(),
            entity.getRedesSociales(),
            entity.getRole().getStringName(),
            ilustradorProfile,
            escritorProfile
        );
    }
}
