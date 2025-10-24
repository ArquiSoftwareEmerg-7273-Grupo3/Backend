package com.drawnet.artcollab.monetizationservice.domain.services;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Monedero;

import java.util.Optional;

public interface MonederoQueryService {

    Optional<Monedero> getByUsuarioId(Long usuarioId);
}
