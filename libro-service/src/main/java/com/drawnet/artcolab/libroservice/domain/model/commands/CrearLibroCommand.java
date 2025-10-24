package com.drawnet.artcolab.libroservice.domain.model.commands;

public record CrearLibroCommand(
        Long escritorId,
        String titulo,
        String sinopsis,
        String urlImagen,
        String urlLibro
) {
    public CrearLibroCommand {
        if (escritorId == null || titulo == null || sinopsis == null || urlImagen == null || urlLibro == null) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
    }
}