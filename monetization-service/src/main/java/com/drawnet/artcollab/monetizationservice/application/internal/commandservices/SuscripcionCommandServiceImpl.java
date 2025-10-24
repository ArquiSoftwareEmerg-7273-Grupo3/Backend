package com.drawnet.artcollab.monetizationservice.application.internal.commandservices;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Suscripcion;
import com.drawnet.artcollab.monetizationservice.domain.model.commands.ActualizarSuscripcionCommand;
import com.drawnet.artcollab.monetizationservice.domain.model.commands.CambiarTipoPlanSuscripcionCommand;
import com.drawnet.artcollab.monetizationservice.domain.model.commands.CrearSuscripcionCommand;
import com.drawnet.artcollab.monetizationservice.domain.model.commands.EliminarSuscripcionCommand;
import com.drawnet.artcollab.monetizationservice.domain.model.valueobjects.Plan;
import com.drawnet.artcollab.monetizationservice.domain.services.SuscripcionCommandService;
import com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.repositories.SuscripcionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SuscripcionCommandServiceImpl implements SuscripcionCommandService {

    private final SuscripcionRepository suscripcionRepository;

    public SuscripcionCommandServiceImpl(SuscripcionRepository suscripcionRepository) {
        this.suscripcionRepository = suscripcionRepository;
    }

    @Override
    public Optional<Suscripcion> handle(CrearSuscripcionCommand command, Plan plan) {
        var suscripcion = new Suscripcion(command, plan);
        var createdSuscripcion = suscripcionRepository.save(suscripcion);
        return Optional.of(createdSuscripcion);
    }

    @Override
    public Optional<Suscripcion> handle(ActualizarSuscripcionCommand command) {
        Optional<Suscripcion> suscripcionOptional = suscripcionRepository.findByUsuarioId(command.usuarioId());
        if (suscripcionOptional.isPresent()) {
            Suscripcion suscripcion = suscripcionOptional.get();
            suscripcion.actualizarSuscripcion(command);
            suscripcionRepository.save(suscripcion);
            return Optional.of(suscripcion);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Suscripcion> handle(CambiarTipoPlanSuscripcionCommand command) {
        Optional<Suscripcion> suscripcionOpt = suscripcionRepository.findByUsuarioId(command.usuarioId());
        if (suscripcionOpt.isEmpty()) return Optional.empty();

        Suscripcion suscripcion = suscripcionOpt.get();
        if (suscripcion.getPlan().equals(command.tipo())) {
            throw new IllegalArgumentException("El usuario ya tiene el plan seleccionado.");
        }
        suscripcion.cambiarTipoPlan(command);
        suscripcionRepository.save(suscripcion);
        return Optional.of(suscripcion);
    }

    @Override
    public Optional<Suscripcion> handle(EliminarSuscripcionCommand command) {
        Optional<Suscripcion> suscripcionOptional = suscripcionRepository.findByUsuarioId(command.usuarioId());
        if (suscripcionOptional.isPresent()) {
            Suscripcion suscripcion = suscripcionOptional.get();
            suscripcionRepository.delete(suscripcion);
            return Optional.of(suscripcion);
        }
        return Optional.empty();
    }
}
