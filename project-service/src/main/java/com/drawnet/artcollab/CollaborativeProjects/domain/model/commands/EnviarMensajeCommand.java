package com.drawnet.artcollab.CollaborativeProjects.domain.model.commands;

import java.util.Objects;

public record EnviarMensajeCommand(Long chatId, Long remitenteId, String texto) {
    public EnviarMensajeCommand {
        Objects.requireNonNull(chatId, "chatId es obligatorio");
        Objects.requireNonNull(remitenteId, "remitenteId es obligatorio");
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("texto es obligatorio");
        }
    }
}
