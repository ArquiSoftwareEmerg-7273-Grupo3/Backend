package com.drawnet.artcollab.CollaborativeProjects.domain.services;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.entities.Postulacion;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.GetAllPostulacionesQuery;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.GetPostulacionesByIlustradorIdQuery;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.GetPostulacionesByProyectoIdQuery;

import java.util.List;

public interface PostulacionQueryService {

    List<Postulacion> handle(GetAllPostulacionesQuery query);

    List<Postulacion> getByIlustradorId(Long ilustradorId);

    List<Postulacion> getByProyectoId(Long proyectoId);
}
