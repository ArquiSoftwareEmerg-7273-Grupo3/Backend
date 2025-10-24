package com.drawnet.artcollab.monetizationservice.domain.services;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Suscripcion;

import java.util.Optional;

public interface SuscripcionQueryService {

    Optional<Suscripcion> getByUsuarioId(Long usuarioId);
}