package com.drawnet.artcollab.iam.domain.model.commands;

import com.drawnet.artcollab.iam.domain.model.entities.Role;

import java.time.LocalDate;
import java.util.Map;

public record SignUpCommand(String username, String password, String ubicacion, String nombres, String apellidos, String telefono, String foto, String descripcion, LocalDate fechaNacimiento, Map<String, String> redesSociales) {
    public SignUpCommand {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
    }
}
