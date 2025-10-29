package com.drawnet.artcolab.libroservice.domain.services;

import com.drawnet.artcolab.libroservice.domain.model.aggregates.Libro;
import com.drawnet.artcolab.libroservice.domain.model.queries.ObtenerCalificacionesDeLibroQuery;
import com.drawnet.artcolab.libroservice.domain.model.queries.ObtenerLibroPorEscritorQuery;
import com.drawnet.artcolab.libroservice.domain.model.valueobjects.Calificacion;

import java.util.List;

public interface LibroQueryService {
    List<Libro> handle(ObtenerLibroPorEscritorQuery query);
    List<Calificacion> handle(ObtenerCalificacionesDeLibroQuery query);
}
