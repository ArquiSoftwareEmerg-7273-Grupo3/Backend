package com.drawnet.artcollab.profiles.application.internal.commandservices;

import com.drawnet.artcollab.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.drawnet.artcollab.profiles.domain.model.aggregates.Ilustrador;
import com.drawnet.artcollab.profiles.domain.model.commands.CreateIlustradorCommand;
import com.drawnet.artcollab.profiles.domain.services.IlustradorCommandService;
import com.drawnet.artcollab.profiles.infrastructure.persistence.jpa.repositories.IlustradorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IlustradorCommandServiceImpl implements IlustradorCommandService {
    private final IlustradorRepository ilustradorRepository;
    private final UserRepository userRepository;

    public IlustradorCommandServiceImpl(IlustradorRepository ilustradorRepository, UserRepository userRepository) {
        this.ilustradorRepository = ilustradorRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Ilustrador> handle(CreateIlustradorCommand command) {
        // Buscar el usuario por ID
        var userOpt = userRepository.findById(command.userId());
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario con ID " + command.userId() + " no encontrado");
        }
        
        var user = userOpt.get();
        
        // Verificar que el usuario no tenga ya un perfil de ilustrador
        if (user.isIlustrador()) {
            throw new IllegalArgumentException("El usuario ya tiene un perfil de ilustrador");
        }
        
        // Crear el ilustrador con la relación al usuario
        var ilustrador = new Ilustrador(command, user);
        ilustradorRepository.save(ilustrador);
        return Optional.of(ilustrador);
    }
}
