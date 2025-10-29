package com.drawnet.artcollab.portafolioservice.interfaces;

import com.drawnet.artcollab.portafolioservice.domain.model.commands.CalificarIlustracionCommand;
import com.drawnet.artcollab.portafolioservice.domain.model.queries.ObtenerCalificacionesDeIlustracionQuery;
import com.drawnet.artcollab.portafolioservice.domain.model.valueobjects.Calificacion;
import com.drawnet.artcollab.portafolioservice.domain.services.PortafolioCommandService;
import com.drawnet.artcollab.portafolioservice.domain.services.PortafolioQueryService;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.CalificarIlustracionResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/calificaciones")
public class CalificacionController {

    private final PortafolioCommandService commandService;
    private final PortafolioQueryService queryService;

    public CalificacionController(PortafolioCommandService commandService, PortafolioQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    // POST: Calificar una ilustración
    //@PostMapping
    //public ResponseEntity<Void> calificarIlustracion(@RequestBody CalificarIlustracionCommand command) {
    //    commandService.handle(command);
    //    return ResponseEntity.ok().build();
    //}

    @PostMapping("/{ilustracionId}")
    public ResponseEntity<Void> calificarIlustracion(
            @PathVariable Long ilustracionId,
            @RequestBody CalificarIlustracionResource resource) {
        CalificarIlustracionCommand command = new CalificarIlustracionCommand(
                ilustracionId, // Se toma de la URL
                resource.usuarioId(),
                resource.puntuacion(),
                resource.comentario()
        );
        commandService.handle(command);
        return ResponseEntity.ok().build();
    }

    // GET: Obtener calificaciones de una ilustración
    @GetMapping("/{ilustracionId}")
    public ResponseEntity<List<Calificacion>> obtenerCalificaciones(@PathVariable Long ilustracionId) {
        List<Calificacion> calificaciones = queryService.handle(new ObtenerCalificacionesDeIlustracionQuery(ilustracionId));
        return ResponseEntity.ok(calificaciones);
    }

}
