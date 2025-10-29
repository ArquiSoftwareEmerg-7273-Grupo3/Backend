package com.drawnet.artcollab.CollaborativeProjects.domain.model.commands;

public record PublicarIlustracionCommand(
        Long ilustradorId,
        String titulo,
        String descripcion,
        String urlImagen,
        boolean publicada
) {
}
