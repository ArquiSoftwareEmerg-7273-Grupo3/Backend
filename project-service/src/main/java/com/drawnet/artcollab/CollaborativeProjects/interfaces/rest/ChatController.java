package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest;



import com.drawnet.artcollab.CollaborativeProjects.application.internal.commandservices.ChatCommandServiceImpl;
import com.drawnet.artcollab.CollaborativeProjects.application.internal.queryservices.ChatQueryServiceImpl;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.ObtenerChatPorIdQuery;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.ObtenerMensajesPorChatQuery;
import com.drawnet.artcollab.CollaborativeProjects.infrastructure.persistence.jpa.repositories.MensajeRepository;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.*;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final ChatCommandServiceImpl chatCommandService;
    private final ChatQueryServiceImpl chatQueryService;
    private final MensajeRepository mensajeRepository;


    public ChatController(ChatCommandServiceImpl chatCommandService, ChatQueryServiceImpl chatQueryService, MensajeRepository mensajeRepository) {
        this.chatCommandService = chatCommandService;
        this.chatQueryService = chatQueryService;
        this.mensajeRepository = mensajeRepository;
    }

    // POST /api/v1/chats
    @PostMapping
    public ResponseEntity<ChatResource> crearChat(@RequestBody CrearChatResource resource) {
        var command = CrearChatCommandFromResourceAssembler.toCommandFromResource(resource);
        var chat = chatCommandService.handle(command).orElseThrow();
        var response = ChatResourceFromEntityAssembler.toResourceFromEntity(chat);
        return ResponseEntity.ok(response);
    }

    // POST /api/v1/chats/{chatId}/mensajes
    @PostMapping("/{chatId}/mensajes")
    public ResponseEntity<Void> enviarMensaje(@PathVariable Long chatId, @RequestBody EnviarMensajeResource resource) {
        var command = EnviarMensajeCommandFromResourceAssembler.toCommandFromResource(chatId, resource);
        chatCommandService.handle(command);
        return ResponseEntity.ok().build();
    }

    // GET /api/v1/chats/{chatId}
    @GetMapping("/{chatId}")
    public ResponseEntity<ChatResource> obtenerChat(@PathVariable Long chatId) {
        var query = new ObtenerChatPorIdQuery(chatId);
        var chat = chatQueryService.handle(query).orElseThrow();
        var response = ChatResourceFromEntityAssembler.toResourceFromEntity(chat);
        return ResponseEntity.ok(response);
    }

    // GET /api/v1/chats/{chatId}/mensajes
    @GetMapping("/{chatId}/mensajes")
    public ResponseEntity<List<MensajeResource>> obtenerMensajes(@PathVariable Long chatId) {
        var query = new ObtenerMensajesPorChatQuery(chatId);
        var mensajes = chatQueryService.handle(query);
        var response = mensajes.stream()
                .map(MensajeResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    //
    @GetMapping("/mensajes/receptor/{remitenteId}")
    public ResponseEntity<List<MensajeConReceptorResource>> obtenerReceptoresPorRemitente(@PathVariable Long remitenteId) {
        var mensajes = mensajeRepository.findByRemitenteId(remitenteId)
                .stream()
                .map(MensajeConReceptorResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(mensajes);
    }

    @GetMapping
    public ResponseEntity<List<ChatResource>> obtenerTodosLosChats() {
        var chats = chatQueryService.obtenerTodos();
        var response = chats.stream()
                .map(ChatResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }



}
