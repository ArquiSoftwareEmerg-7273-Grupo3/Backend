package com.drawnet.artcollab.monetizationservice.interfaces.rest.transform;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Suscripcion;
import com.drawnet.artcollab.monetizationservice.interfaces.rest.resources.SuscripcionResource;

public class SuscripcionResourceFromEntityAssembler {
    public static SuscripcionResource toResourceFromEntity(Suscripcion suscripcion) {
        return new SuscripcionResource(
                suscripcion.getId(),
                suscripcion.getUsuarioId(),
                suscripcion.getFechaInicio(),
                suscripcion.getFechaFin(),
                suscripcion.getPlan().toString()
        );
    }
}
