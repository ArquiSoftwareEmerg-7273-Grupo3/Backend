package com.drawnet.artcollab.portafolioservice.domain.model.commands;

public record ActualizarPortafolioCommand(
        String titulo,
        String descripcion,
        String urlImagen
) {}
