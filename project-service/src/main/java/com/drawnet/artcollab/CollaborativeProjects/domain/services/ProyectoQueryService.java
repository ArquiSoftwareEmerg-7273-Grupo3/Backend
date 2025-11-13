package com.drawnet.artcollab.CollaborativeProjects.domain.services;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Proyecto;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.GetAllProyectosQuery;

import java.util.List;
import java.util.Optional;

public interface ProyectoQueryService {

    List<Proyecto> handle(GetAllProyectosQuery query);
    List<Proyecto> getByEscritorId(Long escritorId);
    Optional<Proyecto> getById(Long id);

}
