package com.drawnet.artcollab.CollaborativeProjects.domain.services;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Postulacion;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.GetAllPostulacionesQuery;

import java.util.List;

public interface PostulacionQueryService {

    List<Postulacion> handle(GetAllPostulacionesQuery query);

    List<Postulacion> getByIlustradorId(Long ilustradorId);

    List<Postulacion> getByProyectoId(Long proyectoId);
}
