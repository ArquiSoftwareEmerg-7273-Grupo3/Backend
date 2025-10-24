package com.drawnet.artcollab.portafolioservice.domain.model.commands;

public record AgregarIlustracionAPortafolioCommand(
        Long portafolioId,
        Long ilustracionId,
        Long ilustradorId,
        String titulo,
        String descripcion,
        String urlImagen
) {
}
