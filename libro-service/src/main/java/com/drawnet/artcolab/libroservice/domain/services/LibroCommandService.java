package com.drawnet.artcolab.libroservice.domain.services;

import com.drawnet.artcolab.libroservice.domain.model.aggregates.Libro;
import com.drawnet.artcolab.libroservice.domain.model.commands.CalificarLibroCommand;
import com.drawnet.artcolab.libroservice.domain.model.commands.CrearLibroCommand;
import com.drawnet.artcolab.libroservice.domain.model.valueobjects.Calificacion;

import java.util.Optional;

public interface LibroCommandService {
    Optional<Libro> handle(CrearLibroCommand command);

    void handle(CalificarLibroCommand command);
}
