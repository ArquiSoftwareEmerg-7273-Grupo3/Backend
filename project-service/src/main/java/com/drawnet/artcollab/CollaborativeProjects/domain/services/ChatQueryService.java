package com.drawnet.artcollab.CollaborativeProjects.domain.services;



import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Chat;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Mensaje;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.ObtenerChatPorIdQuery;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.ObtenerMensajesConReceptorQuery;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.ObtenerMensajesPorChatQuery;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.MensajeConReceptorResource;


import java.util.List;
import java.util.Optional;

public interface ChatQueryService {
    Optional<Chat> handle(ObtenerChatPorIdQuery query);
    List<Mensaje> handle(ObtenerMensajesPorChatQuery query);
    List<MensajeConReceptorResource> handle(ObtenerMensajesConReceptorQuery query);

}
