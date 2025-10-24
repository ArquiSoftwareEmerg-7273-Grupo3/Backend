package com.drawnet.artcollab.portafolioservice.domain.services;

import com.drawnet.artcollab.portafolioservice.domain.model.aggregates.Portafolio;
import com.drawnet.artcollab.portafolioservice.domain.model.entities.Ilustracion;
import com.drawnet.artcollab.portafolioservice.domain.model.queries.*;
import com.drawnet.artcollab.portafolioservice.domain.model.valueobjects.Calificacion;

import java.util.List;
import java.util.Optional;

public interface PortafolioQueryService {
    List<Portafolio> handle(ObtenerPortafoliosPorIlustradorQuery query);
    List<Ilustracion> handle(ObtenerIlustracionesPorPortafolioQuery query);
    List<Calificacion> handle(ObtenerCalificacionesDeIlustracionQuery query);
    List<Ilustracion> handle(ObtenerIlustracionesPublicadasPorIlustradorQuery query);
    Object handle(ObtenerResumenIlustracionQuery query);
}
