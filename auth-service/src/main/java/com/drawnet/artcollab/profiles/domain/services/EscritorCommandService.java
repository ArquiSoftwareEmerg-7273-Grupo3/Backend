package com.drawnet.artcollab.profiles.domain.services;

import com.drawnet.artcollab.profiles.domain.model.aggregates.Escritor;
import com.drawnet.artcollab.profiles.domain.model.commands.CreateEscritorCommand;

import java.util.Optional;

public interface EscritorCommandService {
    Optional<Escritor> handle(CreateEscritorCommand command);
}
