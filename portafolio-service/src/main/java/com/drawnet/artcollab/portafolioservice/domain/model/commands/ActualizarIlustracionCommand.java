package com.drawnet.artcollab.portafolioservice.domain.model.commands;

public record ActualizarIlustracionCommand(
        String titulo,
        String descripcion,
        String urlImagen
) {

}
