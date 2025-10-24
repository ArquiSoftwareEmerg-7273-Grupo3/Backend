package com.drawnet.artcolab.libroservice.interfaces.rest;

import com.drawnet.artcolab.libroservice.application.internal.commandservices.LibroCommandServiceImpl;
import com.drawnet.artcolab.libroservice.application.internal.queryservices.LibroQueryServiceImpl;
import com.drawnet.artcolab.libroservice.domain.model.aggregates.Libro;
import com.drawnet.artcolab.libroservice.domain.model.commands.CalificarLibroCommand;
import com.drawnet.artcolab.libroservice.domain.model.queries.ObtenerCalificacionesDeLibroQuery;
import com.drawnet.artcolab.libroservice.domain.model.valueobjects.Calificacion;
import com.drawnet.artcolab.libroservice.domain.services.LibroCommandService;
import com.drawnet.artcolab.libroservice.domain.services.LibroQueryService;
import com.drawnet.artcolab.libroservice.interfaces.rest.resources.CalificacionResource;
import com.drawnet.artcolab.libroservice.interfaces.rest.resources.CalificarLibroResource;
import com.drawnet.artcolab.libroservice.interfaces.rest.resources.CrearLibroResource;
import com.drawnet.artcolab.libroservice.interfaces.rest.resources.LibroResource;
import com.drawnet.artcolab.libroservice.interfaces.rest.transform.CrearLibroCommandFromResourceAssembler;
import com.drawnet.artcolab.libroservice.interfaces.rest.transform.LibroResourceFromEntityAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/calificaciones")
public class CalificacionController {
    private final LibroCommandService libroCommandService;
    private final LibroQueryService libroQueryService;

    public CalificacionController(LibroCommandServiceImpl libroCommandService, LibroQueryServiceImpl libroQueryService) {
        this.libroCommandService = libroCommandService;
        this.libroQueryService = libroQueryService;
    }

    // POST: Calificar un libro
    @PostMapping("/{libroId}")
    public ResponseEntity<Void> calificarLibro(
            @PathVariable Long libroId,
            @RequestBody CalificarLibroResource resource) {
        CalificarLibroCommand command = new CalificarLibroCommand(
                libroId, // Se toma de la URL
                resource.usuarioId(),
                resource.puntuacion(),
                resource.comentario()
        );
        libroCommandService.handle(command);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{libroId}")
    public ResponseEntity<List<CalificacionResource>> obtenerCalificaciones(@PathVariable Long libroId) {
        List<Calificacion> calificaciones = libroQueryService.handle(new ObtenerCalificacionesDeLibroQuery(libroId));
        List<CalificacionResource> calificacionResources = calificaciones.stream()
                .map(calificacion -> new CalificacionResource(
                        calificacion.getPuntuacion(),
                        calificacion.getComentario(),
                        calificacion.getFecha().toString() // Convertir la fecha a String
                ))
                .toList();
        return ResponseEntity.ok(calificacionResources);
    }


}
