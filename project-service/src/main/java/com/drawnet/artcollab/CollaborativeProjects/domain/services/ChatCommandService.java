package com.drawnet.artcollab.CollaborativeProjects.domain.services;



import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Chat;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CrearChatCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.EnviarMensajeCommand;

import java.util.Optional;

public interface ChatCommandService {
    Optional<Chat> handle(CrearChatCommand command);
    void handle(EnviarMensajeCommand command);
    // Optional<Chat> handle(ActualizarChatCommand command);
}
