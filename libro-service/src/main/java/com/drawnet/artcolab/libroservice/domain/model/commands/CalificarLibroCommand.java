package com.drawnet.artcolab.libroservice.domain.model.commands;

public record CalificarLibroCommand(
    Long libroId,
    Long usuarioId,
    Integer puntuacion,
    String comentario
) {
}
