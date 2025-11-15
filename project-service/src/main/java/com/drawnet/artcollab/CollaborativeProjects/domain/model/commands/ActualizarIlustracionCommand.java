package com.drawnet.artcollab.CollaborativeProjects.domain.model.commands;

public record ActualizarIlustracionCommand(
        String titulo,
        String descripcion,
        String urlImagen
) {

}
