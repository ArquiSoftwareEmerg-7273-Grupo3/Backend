package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources;

public record MensajeResource(
        Long id,
        Long chatId,
        Long remitenteId,
        String texto
) {}
