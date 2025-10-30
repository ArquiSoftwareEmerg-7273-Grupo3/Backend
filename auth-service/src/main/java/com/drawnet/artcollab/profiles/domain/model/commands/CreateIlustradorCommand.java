package com.drawnet.artcollab.profiles.domain.model.commands;

public record CreateIlustradorCommand(String nombreArtistico, Long userId) {
    public CreateIlustradorCommand {
        if (nombreArtistico == null || nombreArtistico.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre artístico es obligatorio");
        }
        if (userId == null) {
            throw new IllegalArgumentException("El ID de usuario es obligatorio");
        }
    }
}
