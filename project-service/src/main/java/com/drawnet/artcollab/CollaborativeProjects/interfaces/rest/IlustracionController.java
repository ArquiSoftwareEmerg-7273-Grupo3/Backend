package com.drawnet.artcollab.CollaborativeProjects.interfaces.rest;

import com.drawnet.artcollab.CollaborativeProjects.application.internal.commandservices.PortafolioCommandServiceImpl;
import com.drawnet.artcollab.CollaborativeProjects.application.internal.queryservices.PortafolioQueryServiceImpl;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.commands.ActualizarIlustracionCommand;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.entities.Ilustracion;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.ObtenerIlustracionesPublicadasPorIlustradorQuery;
import com.drawnet.artcollab.CollaborativeProjects.domain.model.queries.ObtenerResumenIlustracionQuery;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.PublicarIlustracionResource;
import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.transform.PublicarIlustracionCommandFromResourceAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/ilustraciones")
public class IlustracionController {

    private final PortafolioCommandServiceImpl commandService;
    private final PortafolioQueryServiceImpl queryService;

    public IlustracionController(PortafolioCommandServiceImpl commandService, PortafolioQueryServiceImpl queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    //@PostMapping("/publicar")
    //public ResponseEntity<?> publicarIlustracion(@RequestBody PublicarIlustracionResource resource) {
    //    var command = PublicarIlustracionCommandFromResourceAssembler.toCommandFromResource(resource);
    //    var result = commandService.handle(command);
    //    return result.map(ilustracion -> ResponseEntity.ok().body("Ilustración publicada con ID: " + ilustracion.getId()))
    //            .orElse(ResponseEntity.badRequest().build());
    //}

    @PostMapping("/publicar/{ilustradorId}")
    public ResponseEntity<?> publicarIlustracion(
            @PathVariable Long ilustradorId,
            @RequestBody PublicarIlustracionResource resource) {
        var command = PublicarIlustracionCommandFromResourceAssembler.toCommandFromResource(resource, ilustradorId);
        var result = commandService.handle(command);
        return result.map(ilustracion -> ResponseEntity.ok().body("Ilustración publicada con ID: " + ilustracion.getId()))
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/ilustrador/{ilustradorId}/publicadas")
    public ResponseEntity<?> obtenerIlustracionesPublicadas(@PathVariable Long ilustradorId) {
        var result = queryService.handle(new ObtenerIlustracionesPublicadasPorIlustradorQuery(ilustradorId));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{ilustracionId}/resumen")
    public ResponseEntity<?> obtenerResumenIlustracion(@PathVariable Long ilustracionId) {
        var result = queryService.handle(new ObtenerResumenIlustracionQuery(ilustracionId));
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{ilustracionId}")
    public ResponseEntity<Void> eliminarIlustracion(@PathVariable Long ilustracionId) {
        commandService.eliminarIlustracion(ilustracionId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{ilustracionId}")
    public ResponseEntity<Ilustracion> actualizarIlustracion(
            @PathVariable Long ilustracionId,
            @RequestBody ActualizarIlustracionCommand command) {
        Optional<Ilustracion> ilustracionOpt = commandService.actualizarIlustracion(ilustracionId, command);
        return ilustracionOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
