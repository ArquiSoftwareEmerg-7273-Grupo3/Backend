package com.drawnet.artcollab.CollaborativeProjects.domain.model.commands;

public record ActualizarPortafolioCommand(
        String titulo,
        String descripcion,
        String urlImagen
) {}
