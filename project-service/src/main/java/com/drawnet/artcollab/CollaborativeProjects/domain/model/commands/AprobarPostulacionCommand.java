package com.drawnet.artcollab.CollaborativeProjects.domain.model.commands;

public record AprobarPostulacionCommand(
    Long postulacionId,
    Long escritorId,
    String respuesta
) {}
