package com.drawnet.artcollab.monetizationservice.interfaces.rest.transform;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Monedero;
import com.drawnet.artcollab.monetizationservice.interfaces.rest.resources.MonederoResource;

public class MonederoResourceFromEntityAssembler {
    public static MonederoResource toResourceFromEntity(Monedero monedero) {
        return new MonederoResource(monedero.getId(), monedero.getUsuarioId(), monedero.getSaldo());
    }
}
