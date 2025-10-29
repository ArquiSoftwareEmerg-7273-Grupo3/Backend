package com.drawnet.artcollab.monetizationservice.domain.services;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Donacion;

import java.util.List;
import java.util.Optional;

public interface DonacionQueryService {

    Optional<Donacion> getById(Long id);

    List<Donacion> getByDonanteId(Long donanteId);

    List<Donacion> getByReceptorId(Long receptorId);
}
