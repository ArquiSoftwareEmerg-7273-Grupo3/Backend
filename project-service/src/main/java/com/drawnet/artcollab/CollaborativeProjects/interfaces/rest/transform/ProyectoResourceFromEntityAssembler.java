package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Proyecto;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.ProyectoResource;

public class ProyectoResourceFromEntityAssembler {

    public static ProyectoResource toResourceFromEntity(Proyecto entity) {
        return new ProyectoResource(
                entity.getId(),
                entity.getEscritorId(),
                entity.getTitulo(),
                entity.getDescripcion(),
                entity.getFechaFin(),
                entity.getFechaInicio(),
                entity.getPresupuesto(),
                entity.getMaxPostulaciones()
        );
    }
}
