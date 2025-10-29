package com.drawnet.artcollab.iam.domain.model.commands;

public record SignInCommand(String username, String password) {
    public SignInCommand {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
    }
}
