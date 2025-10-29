package com.drawnet.artcollab.monetizationservice.domain.services;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Donacion;
import com.drawnet.artcollab.monetizationservice.domain.model.commands.CrearDonacionCommand;

import java.util.Optional;

public interface DonacionCommandService {

    Optional<Donacion> handle(CrearDonacionCommand command);
}
