package com.drawnet.artcollab.CollaborativeProjects.domain.model.commands;

public record RechazarPostulacionCommand(
    Long postulacionId,
    Long escritorId,
    String razon
) {}
