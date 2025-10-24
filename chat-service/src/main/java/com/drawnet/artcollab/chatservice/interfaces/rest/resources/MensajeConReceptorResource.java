package com.drawnet.artcollab.chatservice.interfaces.rest.resources;

public record MensajeConReceptorResource(
        Long receptorId,
        String texto
) {
}
