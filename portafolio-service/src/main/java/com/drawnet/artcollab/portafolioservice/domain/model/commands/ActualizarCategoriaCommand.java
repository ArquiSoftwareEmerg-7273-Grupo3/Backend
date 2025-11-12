package com.drawnet.artcollab.portafolioservice.domain.model.commands;

public record ActualizarCategoriaCommand(
        String nombre,
        String descripcion,
        Integer orden
) {
}
