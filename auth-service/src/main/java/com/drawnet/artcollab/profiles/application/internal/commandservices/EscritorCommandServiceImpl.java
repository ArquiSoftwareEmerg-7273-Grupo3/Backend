package com.drawnet.artcollab.profiles.application.internal.commandservices;

import com.drawnet.artcollab.profiles.domain.model.aggregates.Escritor;
import com.drawnet.artcollab.profiles.domain.model.commands.CreateEscritorCommand;
import com.drawnet.artcollab.profiles.domain.services.EscritorCommandService;
import com.drawnet.artcollab.profiles.infrastructure.persistence.jpa.repositories.EscritorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EscritorCommandServiceImpl implements EscritorCommandService {
    private final EscritorRepository escritorRepository;

    public EscritorCommandServiceImpl(EscritorRepository escritorRepository) {
        this.escritorRepository = escritorRepository;
    }

    @Override
    public Optional<Escritor> handle(CreateEscritorCommand command) {
        var escritor = new Escritor(command);
        escritorRepository.save(escritor);
        return Optional.of(escritor);
    }
}
