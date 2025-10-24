package com.drawnet.artcollab.CollaborativeProjects.domain.services;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Proyecto;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.GetAllProyectosQuery;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.GetProyectosByEscritorIdQuery;

import java.util.List;

public interface ProyectoQueryService {

    List<Proyecto> handle(GetAllProyectosQuery query);
    List<Proyecto> getByEscritorId(Long escritorId);

}
