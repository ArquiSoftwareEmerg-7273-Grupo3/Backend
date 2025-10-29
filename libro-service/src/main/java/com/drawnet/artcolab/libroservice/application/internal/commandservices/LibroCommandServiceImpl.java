package com.drawnet.artcolab.libroservice.application.internal.commandservices;

import com.drawnet.artcolab.libroservice.domain.model.aggregates.Libro;
import com.drawnet.artcolab.libroservice.domain.model.commands.CalificarLibroCommand;
import com.drawnet.artcolab.libroservice.domain.model.commands.CrearLibroCommand;
import com.drawnet.artcolab.libroservice.domain.model.valueobjects.Calificacion;
import com.drawnet.artcolab.libroservice.domain.services.LibroCommandService;
import com.drawnet.artcolab.libroservice.infrastructure.persistence.jpa.repositories.LibroRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LibroCommandServiceImpl implements LibroCommandService {

    private final LibroRepository libroRepository;

    public LibroCommandServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    public Optional<Libro> handle(CrearLibroCommand command) {
        Libro libro = new Libro(command.escritorId(), command.titulo(), command.sinopsis(), command.urlImagen(),command.urlLibro());
        return Optional.of(libroRepository.save(libro));
    }

    @Override
    public void handle(CalificarLibroCommand command) {
        libroRepository.findById(command.libroId()).ifPresent(ilustracion -> {
            ilustracion.agregarCalificacion(command.usuarioId(), command.puntuacion(), command.comentario());
            System.out.println("Calificación agregada: " + command.puntuacion() + " - " + command.comentario());
            libroRepository.save(ilustracion);
        });
    }


}
