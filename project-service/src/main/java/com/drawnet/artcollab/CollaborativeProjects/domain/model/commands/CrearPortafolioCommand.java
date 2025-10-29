package com.drawnet.artcollab.CollaborativeProjects.domain.model.commands;

import java.util.Objects;

public record CrearPortafolioCommand(
        Long ilustradorId,
        String titulo,
        String descripcion,
        String urlImagen
) {
    public CrearPortafolioCommand {
        Objects.requireNonNull(ilustradorId, "El ilustradorId es obligatorio");
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }
        if (urlImagen == null || urlImagen.trim().isEmpty()) {
            throw new IllegalArgumentException("La URL de la imagen es obligatoria");
        }
    }
}
