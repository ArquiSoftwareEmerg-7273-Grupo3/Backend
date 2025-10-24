package com.drawnet.artcollab.chatservice.application.internal.queryservices;

import com.drawnet.artcollab.chatservice.domain.model.aggregates.Chat;
import com.drawnet.artcollab.chatservice.domain.model.aggregates.Mensaje;
import com.drawnet.artcollab.chatservice.domain.model.queries.ObtenerChatPorIdQuery;
import com.drawnet.artcollab.chatservice.domain.model.queries.ObtenerMensajesConReceptorQuery;
import com.drawnet.artcollab.chatservice.domain.model.queries.ObtenerMensajesPorChatQuery;
import com.drawnet.artcollab.chatservice.domain.services.ChatQueryService;
import com.drawnet.artcollab.chatservice.infrastructure.persistance.jpa.repositories.ChatRepository;
import com.drawnet.artcollab.chatservice.infrastructure.persistance.jpa.repositories.MensajeRepository;
import com.drawnet.artcollab.chatservice.interfaces.rest.resources.MensajeConReceptorResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatQueryServiceImpl implements ChatQueryService {
    private final ChatRepository chatRepository;
    private final MensajeRepository mensajeRepository;

    public ChatQueryServiceImpl(ChatRepository chatRepository, MensajeRepository mensajeRepository) {
        this.chatRepository = chatRepository;
        this.mensajeRepository = mensajeRepository;
    }

    @Override
    public Optional<Chat> handle(ObtenerChatPorIdQuery query) {
        return chatRepository.findById(query.chatId());
    }

    @Override
    public List<Mensaje> handle(ObtenerMensajesPorChatQuery query) {
        return mensajeRepository.findByChatId(query.chatId());
    }

    @Override
    public List<MensajeConReceptorResource> handle(ObtenerMensajesConReceptorQuery query) {
        Long remitenteId = query.remitenteId();

        List<Mensaje> mensajes = mensajeRepository.findByRemitenteId(remitenteId);

        return mensajes.stream().map(mensaje -> {
            Chat chat = mensaje.getChat();
            Long receptorId = chat.getUsuario1Id().equals(remitenteId)
                    ? chat.getUsuario2Id()
                    : chat.getUsuario1Id();

            return new MensajeConReceptorResource(receptorId, mensaje.getTexto());
        }).toList();
    }

    public List<Chat> obtenerTodos() {
        return chatRepository.findAll();
    }


}
