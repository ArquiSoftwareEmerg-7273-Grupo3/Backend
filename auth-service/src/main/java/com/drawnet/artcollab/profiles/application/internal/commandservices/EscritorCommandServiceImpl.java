package com.drawnet.artcollab.profiles.application.internal.commandservices;

import com.drawnet.artcollab.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.drawnet.artcollab.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.drawnet.artcollab.iam.domain.model.valueobjects.Roles;
import com.drawnet.artcollab.profiles.domain.model.aggregates.Escritor;
import com.drawnet.artcollab.profiles.domain.model.commands.CreateEscritorCommand;
import com.drawnet.artcollab.profiles.domain.services.EscritorCommandService;
import com.drawnet.artcollab.profiles.infrastructure.persistence.jpa.repositories.EscritorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EscritorCommandServiceImpl implements EscritorCommandService {
    private final EscritorRepository escritorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public EscritorCommandServiceImpl(EscritorRepository escritorRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.escritorRepository = escritorRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Escritor> handle(CreateEscritorCommand command) {
        // Buscar el usuario por ID
        var userOpt = userRepository.findById(command.userId());
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario con ID " + command.userId() + " no encontrado");
        }
        
        var user = userOpt.get();
        
        // Verificar que el usuario no tenga ya un perfil de escritor
        if (user.isEscritor()) {
            throw new IllegalArgumentException("El usuario ya tiene un perfil de escritor");
        }
        
    // Crear el escritor con la relación al usuario
    var escritor = new Escritor(command, user);
    escritorRepository.save(escritor);

    // Cambiar rol del usuario a ESCRITOR
    var role = roleRepository.findByName(Roles.ESCRITOR)
        .orElseThrow(() -> new RuntimeException("Role ESCRITOR no encontrado"));
    user.setRole(role);
    userRepository.save(user);

    return Optional.of(escritor);
    }
}
