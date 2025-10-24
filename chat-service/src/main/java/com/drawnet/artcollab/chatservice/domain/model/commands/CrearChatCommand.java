package com.drawnet.artcollab.chatservice.domain.model.commands;

import java.util.Objects;

public record CrearChatCommand(Long usuario1id, Long usuario2id) {
    public CrearChatCommand {
        Objects.requireNonNull(usuario1id, "usuario1id es obligatorio");
        Objects.requireNonNull(usuario2id, "usuario2id es obligatorio");
    }
}
