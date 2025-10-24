package com.drawnet.artcollab.iam.domain.model.commands;

import com.drawnet.artcollab.iam.domain.model.entities.Role;

public record SignUpCommand(String username, String password, Role role) {
    public SignUpCommand {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        if (role == null) {
            throw new IllegalArgumentException("El rol es obligatorio");
        }
    }
}
