package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.entities.Ilustracion;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.IlustracionResource;

public class IlustracionResourceFromEntityAssembler {
    public static IlustracionResource toResource(Ilustracion ilustracion) {
        return new IlustracionResource(
                ilustracion.getId(),
                ilustracion.getTitulo(),
                ilustracion.getDescripcion(),
                ilustracion.getUrlImagen()
        );
    }
}
