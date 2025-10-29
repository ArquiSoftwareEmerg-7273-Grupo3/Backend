package com.drawnet.artcollab.CollaborativeProjects.domain.model.commands;

import java.util.Date;

public record CreatePostulacionCommand(Long proyectoId, Long ilustradorId, String estado, Date fecha) {
    public CreatePostulacionCommand {
        if (proyectoId == null || proyectoId < 0) {
            throw new IllegalArgumentException("ProyectoId no puede ser nulo o menor que 0");
        }
        if (ilustradorId == null || ilustradorId < 0) {
            throw new IllegalArgumentException("IlustradorId no puede ser nulo o menor que 0");
        }
        if (estado == null || estado.isEmpty()) {
            throw new IllegalArgumentException("Estado no puede ser nulo o vacio");
        }
        if (fecha == null || fecha.toString().isEmpty()) {
            throw new IllegalArgumentException("Fecha no puede ser nulo");
        }
    }
}
