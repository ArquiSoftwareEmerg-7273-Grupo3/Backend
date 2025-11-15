package com.drawnet.artcollab.CollaborativeProjects.domain.services;

import com.drawnet.artcollab.CollaborativeProjects.domain.model.aggregates.Portafolio;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.*;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.valueobjects.Calificacion;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.entities.Ilustracion;

import java.util.List;

public interface PortafolioQueryService {
    List<Portafolio> handle(ObtenerPortafoliosPorIlustradorQuery query);
    List<Ilustracion> handle(ObtenerIlustracionesPorPortafolioQuery query);
    List<Calificacion> handle(ObtenerCalificacionesDeIlustracionQuery query);
    List<Ilustracion> handle(ObtenerIlustracionesPublicadasPorIlustradorQuery query);
    Object handle(ObtenerResumenIlustracionQuery query);
}
