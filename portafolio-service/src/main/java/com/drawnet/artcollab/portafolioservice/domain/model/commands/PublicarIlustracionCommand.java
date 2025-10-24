package com.drawnet.artcollab.portafolioservice.domain.model.commands;

public record PublicarIlustracionCommand(
        Long ilustradorId,
        String titulo,
        String descripcion,
        String urlImagen,
        boolean publicada
) {
}
