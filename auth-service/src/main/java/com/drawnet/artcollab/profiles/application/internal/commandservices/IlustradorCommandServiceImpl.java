package com.drawnet.artcollab.profiles.application.internal.commandservices;

import com.drawnet.artcollab.profiles.domain.model.aggregates.Ilustrador;
import com.drawnet.artcollab.profiles.domain.model.commands.CreateIlustradorCommand;
import com.drawnet.artcollab.profiles.domain.services.IlustradorCommandService;
import com.drawnet.artcollab.profiles.infrastructure.persistence.jpa.repositories.IlustradorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IlustradorCommandServiceImpl implements IlustradorCommandService {
    private final IlustradorRepository patientRepository;

    public IlustradorCommandServiceImpl(IlustradorRepository ilustradorRepository) {
        this.patientRepository = ilustradorRepository;
    }

    @Override
    public Optional<Ilustrador> handle(CreateIlustradorCommand command) {
        var ilustrador = new Ilustrador(command);
        patientRepository.save(ilustrador);
        return Optional.of(ilustrador);
    }
}
