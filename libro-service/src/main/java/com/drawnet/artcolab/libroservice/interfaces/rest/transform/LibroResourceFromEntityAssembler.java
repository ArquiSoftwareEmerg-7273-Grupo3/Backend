package com.drawnet.artcolab.libroservice.interfaces.rest.transform;

import com.drawnet.artcolab.libroservice.domain.model.aggregates.Libro;
import com.drawnet.artcolab.libroservice.interfaces.rest.resources.LibroResource;

public class LibroResourceFromEntityAssembler {
    public static LibroResource toResourceFromEntity(Libro libro) {
        return new LibroResource(
                libro.getId(),
                libro.getTitulo(),
                libro.getSinopsis(),
                libro.getUrlImagen(),
                libro.getUrlLibro()
        );
    }
}
