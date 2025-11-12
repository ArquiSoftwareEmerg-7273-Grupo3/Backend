package com.drawnet.artcollab.portafolioservice.domain.model.commands;

public record CrearCategoriaCommand(
        Long portafolioId,
        String nombre,
        String descripcion,
        Integer orden
) {
}
