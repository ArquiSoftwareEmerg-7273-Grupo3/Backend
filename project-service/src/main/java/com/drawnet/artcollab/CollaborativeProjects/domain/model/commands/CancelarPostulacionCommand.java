package com.drawnet.artcollab.CollaborativeProjects.domain.model.commands;

public record CancelarPostulacionCommand(
    Long postulacionId,
    Long ilustradorId
) {}
