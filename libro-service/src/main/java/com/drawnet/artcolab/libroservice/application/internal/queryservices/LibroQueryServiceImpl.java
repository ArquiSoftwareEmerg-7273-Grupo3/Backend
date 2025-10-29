package com.drawnet.artcolab.libroservice.application.internal.queryservices;

import com.drawnet.artcolab.libroservice.domain.model.aggregates.Libro;
import com.drawnet.artcolab.libroservice.domain.model.queries.ObtenerCalificacionesDeLibroQuery;
import com.drawnet.artcolab.libroservice.domain.model.queries.ObtenerLibroPorEscritorQuery;
import com.drawnet.artcolab.libroservice.domain.model.valueobjects.Calificacion;
import com.drawnet.artcolab.libroservice.domain.services.LibroQueryService;
import com.drawnet.artcolab.libroservice.infrastructure.persistence.jpa.repositories.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroQueryServiceImpl implements LibroQueryService {

    private final LibroRepository libroRepository;

    public LibroQueryServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    public List<Libro> handle(ObtenerLibroPorEscritorQuery query) {
        return libroRepository.findByEscritorId(query.escritorId());
    }

    @Override
    public List<Calificacion> handle(ObtenerCalificacionesDeLibroQuery query) {
        Optional<Libro> libroOpt = libroRepository.findById(query.libroId());
        if (libroOpt.isPresent()) {
            Libro libro = libroOpt.get();
            // Forzar la inicialización de las calificaciones
            libro.getCalificaciones().size();
            return libro.getCalificaciones();
        }
        return List.of();
    }

}
