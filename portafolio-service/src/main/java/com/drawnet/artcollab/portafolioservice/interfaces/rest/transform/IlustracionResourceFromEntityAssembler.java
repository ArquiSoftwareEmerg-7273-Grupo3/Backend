package com.drawnet.artcollab.portafolioservice.interfaces.rest.transform;

import com.drawnet.artcollab.portafolioservice.domain.model.entities.Ilustracion;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.IlustracionResource;

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
