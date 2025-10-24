package com.drawnet.artcollab.CollaborativeProjects.domain.model.commands;

import java.util.Date;

public record CreateProyectoCommand(Long escritorId, String titulo, String descripcion, String urlImagen, Date fecha) {
    public CreateProyectoCommand {
        if (escritorId == null || escritorId < 0) {
            throw new IllegalArgumentException("EscritorId no puede ser nulo o menor que 0");
        }
        if (titulo == null || titulo.isEmpty()) {
            throw new IllegalArgumentException("Titulo no puede ser nulo o vacio");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new IllegalArgumentException("Descripcion no puede ser nulo o vacio");
        }
        if (urlImagen == null || urlImagen.isEmpty()) {
            throw new IllegalArgumentException("UrlImagen no puede ser nulo o vacio");
        }
        if (fecha == null || fecha.toString().isEmpty()) {
            throw new IllegalArgumentException("Fecha no puede ser nulo");
        }
    }
}
