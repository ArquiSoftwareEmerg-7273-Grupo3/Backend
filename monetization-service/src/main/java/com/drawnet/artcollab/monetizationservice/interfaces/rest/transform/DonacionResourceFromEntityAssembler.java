package com.drawnet.artcollab.monetizationservice.interfaces.rest.transform;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Donacion;
import com.drawnet.artcollab.monetizationservice.interfaces.rest.resources.DonacionResource;

public class DonacionResourceFromEntityAssembler {
    public static DonacionResource toResourceFromEntity(Donacion donacion) {
        return new DonacionResource(
                donacion.getId(),
                donacion.getDonanteId(),
                donacion.getReceptorId(),
                donacion.getMonto()
        );
    }
}
