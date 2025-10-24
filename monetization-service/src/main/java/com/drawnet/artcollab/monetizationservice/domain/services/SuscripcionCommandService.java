package com.drawnet.artcollab.monetizationservice.domain.services;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Suscripcion;
import com.drawnet.artcollab.monetizationservice.domain.model.commands.ActualizarSuscripcionCommand;
import com.drawnet.artcollab.monetizationservice.domain.model.commands.CambiarTipoPlanSuscripcionCommand;
import com.drawnet.artcollab.monetizationservice.domain.model.commands.CrearSuscripcionCommand;
import com.drawnet.artcollab.monetizationservice.domain.model.commands.EliminarSuscripcionCommand;
import com.drawnet.artcollab.monetizationservice.domain.model.valueobjects.Plan;

import java.util.Optional;

public interface SuscripcionCommandService {

    Optional<Suscripcion> handle(CrearSuscripcionCommand command, Plan plan);

    Optional<Suscripcion> handle(ActualizarSuscripcionCommand command);

    Optional<Suscripcion> handle(CambiarTipoPlanSuscripcionCommand command);

    Optional<Suscripcion> handle(EliminarSuscripcionCommand command);
}