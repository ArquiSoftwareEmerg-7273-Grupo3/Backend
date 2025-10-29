package com.drawnet.artcollab.profiles.domain.services;

import com.drawnet.artcollab.profiles.domain.model.aggregates.Ilustrador;
import com.drawnet.artcollab.profiles.domain.model.commands.CreateIlustradorCommand;

import java.util.Optional;

public interface IlustradorCommandService {
    Optional<Ilustrador> handle(CreateIlustradorCommand command);
}
