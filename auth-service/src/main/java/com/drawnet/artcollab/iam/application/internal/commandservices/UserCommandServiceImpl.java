package com.drawnet.artcollab.iam.application.internal.commandservices;


import com.drawnet.artcollab.iam.application.internal.outboundservices.hashing.HashingService;
import com.drawnet.artcollab.iam.application.internal.outboundservices.tokens.TokenService;
import com.drawnet.artcollab.iam.domain.model.aggregates.User;
import com.drawnet.artcollab.iam.domain.model.commands.SignInCommand;
import com.drawnet.artcollab.iam.domain.model.commands.SignUpCommand;
import com.drawnet.artcollab.iam.domain.model.valueobjects.Roles;
import com.drawnet.artcollab.iam.domain.services.UserCommandService;
import com.drawnet.artcollab.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.drawnet.artcollab.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private static final Roles DEFAULT_ROLE = Roles.GENERAL;

    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;

    public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService, TokenService tokenService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByUsername(command.username()))
            throw new RuntimeException("Username already exists");
        if (!roleRepository.existsByName(DEFAULT_ROLE))
            throw new RuntimeException("Role doesnt exists");

        var role = roleRepository.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        var user = new User(command.username(), hashingService.encode(command.password()), role,
        command.ubicacion(), command.nombres(), command.apellidos(), command.telefono(),
        command.foto(), command.descripcion(), command.fechaNacimiento(), command.redesSociales()
        );
        userRepository.save(user);
        return userRepository.findByUsername(command.username());
    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {
        var user = userRepository.findByUsername(command.username())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!hashingService.matches(command.password(), user.getPassword()))
            throw new RuntimeException("Invalid password");
        var token = tokenService.generateToken(user.getUsername());
        return Optional.of(new ImmutablePair<>(user, token));
    }
}
