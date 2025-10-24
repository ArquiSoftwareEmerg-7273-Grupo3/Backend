package com.drawnet.artcollab.chatservice.interfaces.rest.resources;

public record MensajeResource(
        Long id,
        Long chatId,
        Long remitenteId,
        String texto
) {}
