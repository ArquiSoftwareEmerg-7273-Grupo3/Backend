package com.drawnet.artcollab.portafolioservice.domain.services;

import com.drawnet.artcollab.portafolioservice.domain.model.aggregates.Portafolio;
import com.drawnet.artcollab.portafolioservice.domain.model.entities.Categoria;
import com.drawnet.artcollab.portafolioservice.domain.model.entities.Ilustracion;
import com.drawnet.artcollab.portafolioservice.domain.model.queries.*;
import com.drawnet.artcollab.portafolioservice.domain.model.valueobjects.Calificacion;

import java.util.List;
import java.util.Optional;

public interface PortafolioQueryService {
    // Queries de Portafolio
    List<Portafolio> handle(ObtenerPortafoliosPorIlustradorQuery query);
    
    // Queries de Categoría
    List<Categoria> handle(ObtenerCategoriasPorPortafolioQuery query);
    Optional<Categoria> handle(ObtenerCategoriaConIlustracionesQuery query);
    
    // Queries de Ilustración
    List<Ilustracion> handle(ObtenerIlustracionesPorCategoriaQuery query);
    List<Ilustracion> handle(ObtenerIlustracionesPublicadasPorIlustradorQuery query);
    Object handle(ObtenerResumenIlustracionQuery query);
    
    // Queries de Calificación
    List<Calificacion> handle(ObtenerCalificacionesDeIlustracionQuery query);
    
    // DEPRECATED - mantener por compatibilidad
    @Deprecated
    List<Ilustracion> handle(ObtenerIlustracionesPorPortafolioQuery query);
}
