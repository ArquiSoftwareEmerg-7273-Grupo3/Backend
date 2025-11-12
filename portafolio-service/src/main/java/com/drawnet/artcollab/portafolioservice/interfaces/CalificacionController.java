package com.drawnet.artcollab.portafolioservice.interfaces;

import com.drawnet.artcollab.portafolioservice.domain.model.commands.CalificarIlustracionCommand;
import com.drawnet.artcollab.portafolioservice.domain.model.queries.ObtenerCalificacionesDeIlustracionQuery;
import com.drawnet.artcollab.portafolioservice.domain.model.valueobjects.Calificacion;
import com.drawnet.artcollab.portafolioservice.domain.services.PortafolioCommandService;
import com.drawnet.artcollab.portafolioservice.domain.services.PortafolioQueryService;
import com.drawnet.artcollab.portafolioservice.infrastructure.security.JwtService;
import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.CalificarIlustracionResource;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Calificaciones", description = "Gestión de calificaciones de ilustraciones")
@RequestMapping("/api/v1/calificaciones")
public class CalificacionController {
    private final JwtService jwtService;
    private final PortafolioCommandService commandService;
    private final PortafolioQueryService queryService;

    public CalificacionController(JwtService jwtService, PortafolioCommandService commandService, PortafolioQueryService queryService) {
        this.jwtService = jwtService;
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
            @Parameter(hidden = true) @RequestHeader("Authorization") String authHeader,
            @PathVariable Long ilustracionId,
            @RequestBody CalificarIlustracionResource resource) {
                try {
                    String token = jwtService.cleanToken(authHeader);
                    Long userId = jwtService.extractUserId(token);
                  
            CalificarIlustracionCommand command = new CalificarIlustracionCommand(
                            ilustracionId,
                            userId,
                            resource.puntuacion(),
                            resource.comentario()
                    );
                    commandService.handle(command);
        return ResponseEntity.ok().build();
                } catch (Exception e) {
                    return ResponseEntity.status(401).build();
                }
      
        
    }

    // GET: Obtener calificaciones de una ilustración
    @GetMapping("/{ilustracionId}")
    public ResponseEntity<List<Calificacion>> obtenerCalificaciones(@PathVariable Long ilustracionId) {
        List<Calificacion> calificaciones = queryService.handle(new ObtenerCalificacionesDeIlustracionQuery(ilustracionId));
        return ResponseEntity.ok(calificaciones);
    }

}
