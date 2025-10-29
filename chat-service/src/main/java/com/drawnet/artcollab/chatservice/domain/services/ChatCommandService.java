package com.drawnet.artcollab.chatservice.domain.services;

import com.drawnet.artcollab.chatservice.domain.model.aggregates.Chat;
import com.drawnet.artcollab.chatservice.domain.model.commands.CrearChatCommand;
import com.drawnet.artcollab.chatservice.domain.model.commands.EnviarMensajeCommand;

import java.util.Optional;

public interface ChatCommandService {
    Optional<Chat> handle(CrearChatCommand command);
    void handle(EnviarMensajeCommand command);
    // Optional<Chat> handle(ActualizarChatCommand command);
}
