package com.drawnet.artcollab.monetizationservice.domain.services;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Monedero;
import com.drawnet.artcollab.monetizationservice.domain.model.commands.ActualizarMonederoCommand;
import com.drawnet.artcollab.monetizationservice.domain.model.commands.CrearMonederoCommand;

import java.util.Optional;

public interface MonederoCommandService {

    Optional<Monedero> handle(CrearMonederoCommand command);

    Optional<Monedero> handle(ActualizarMonederoCommand command);
}