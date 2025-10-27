package com.drawnet.artcollab.CollaborativeProjects.application.internal.commandservices;


import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Chat;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Mensaje;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.CrearChatCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.EnviarMensajeCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.services.ChatCommandService;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.external.clients.UsuarioCliente;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories.ChatRepository;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories.MensajeRepository;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ChatCommandServiceImpl implements ChatCommandService {

    private final ChatRepository chatRepository;
    private final MensajeRepository mensajeRepository;
    private final UsuarioCliente usuarioClient;

    public ChatCommandServiceImpl(ChatRepository chatRepository, MensajeRepository mensajeRepository, UsuarioCliente usuarioClient) {
        this.chatRepository = chatRepository;
        this.mensajeRepository = mensajeRepository;
        this.usuarioClient = usuarioClient;

    }

    @Override
    public Optional<Chat> handle(CrearChatCommand command) {
        try {
            // Validar existencia de ambos usuarios antes de crear el chat
            usuarioClient.verificarUsuario(command.usuario1id());
            usuarioClient.verificarUsuario(command.usuario2id());
        } catch (FeignException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Uno o ambos usuarios no existen.");
        }

        Chat nuevoChat = new Chat(command.usuario1id(), command.usuario2id());
        Chat chatGuardado = chatRepository.save(nuevoChat);
        return Optional.of(chatGuardado);
    }

    //@Override
    //public Optional<Chat> handle(CrearChatCommand command) {
    //    Chat nuevoChat = new Chat(command.usuario1id(), command.usuario2id());
    //    Chat chatGuardado = chatRepository.save(nuevoChat);
    //    return Optional.of(chatGuardado);
    //}



    @Override
    public void handle(EnviarMensajeCommand command) {
        Optional<Chat> chatOpt = chatRepository.findById(command.chatId());

        if (chatOpt.isEmpty()) {
            throw new IllegalArgumentException("Chat no encontrado con ID: " + command.chatId());
        }

        Chat chat = chatOpt.get();

        if (!chat.isActivo()) {
            throw new IllegalStateException("No se puede enviar mensajes: el chat está inactivo.");
        }

        Mensaje mensaje = new Mensaje(chat, command.remitenteId(), command.texto());
        mensajeRepository.save(mensaje);
    }


}
