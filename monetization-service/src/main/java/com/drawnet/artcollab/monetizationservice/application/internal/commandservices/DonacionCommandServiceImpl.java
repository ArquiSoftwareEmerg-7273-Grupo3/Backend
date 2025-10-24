package com.drawnet.artcollab.monetizationservice.application.internal.commandservices;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Donacion;
import com.drawnet.artcollab.monetizationservice.domain.model.commands.CrearDonacionCommand;
import com.drawnet.artcollab.monetizationservice.domain.services.DonacionCommandService;
import com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.repositories.DonacionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DonacionCommandServiceImpl implements DonacionCommandService {

    private final DonacionRepository donacionRepository;

    public DonacionCommandServiceImpl(DonacionRepository donacionRepository) {
        this.donacionRepository = donacionRepository;
    }

    @Override
    public Optional<Donacion> handle(CrearDonacionCommand command) {
        var donacion = new Donacion(command);
        var createdDonacion = donacionRepository.save(donacion);
        return Optional.of(createdDonacion);
    }
}