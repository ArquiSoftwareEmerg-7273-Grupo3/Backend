package com.drawnet.artcollab.monetizationservice.interfaces.rest;

import com.drawnet.artcollab.monetizationservice.application.internal.commandservices.SuscripcionCommandServiceImpl;
import com.drawnet.artcollab.monetizationservice.application.internal.queryservices.SuscripcionQueryServiceImpl;
import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Suscripcion;
import com.drawnet.artcollab.monetizationservice.domain.model.commands.CambiarTipoPlanSuscripcionCommand;
import com.drawnet.artcollab.monetizationservice.domain.model.commands.EliminarSuscripcionCommand;
import com.drawnet.artcollab.monetizationservice.domain.model.valueobjects.Plan;
import com.drawnet.artcollab.monetizationservice.interfaces.rest.resources.ActualizarSuscripcionResource;
import com.drawnet.artcollab.monetizationservice.interfaces.rest.resources.CrearSuscripcionResource;
import com.drawnet.artcollab.monetizationservice.interfaces.rest.resources.SuscripcionResource;
import com.drawnet.artcollab.monetizationservice.interfaces.rest.transform.ActualizarSuscripcionCommandFromResourceAssembler;
import com.drawnet.artcollab.monetizationservice.interfaces.rest.transform.CrearSuscripcionCommandFromResourceAssembler;
import com.drawnet.artcollab.monetizationservice.interfaces.rest.transform.SuscripcionResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/suscripciones", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Suscripción", description = "Operaciones relacionadas con suscripciones")
public class SuscripcionController {

    private final SuscripcionCommandServiceImpl suscripcionCommandService;
    private final SuscripcionQueryServiceImpl suscripcionQueryService;

    public SuscripcionController(SuscripcionCommandServiceImpl suscripcionCommandService, SuscripcionQueryServiceImpl suscripcionQueryService){
        this.suscripcionCommandService = suscripcionCommandService;
        this.suscripcionQueryService = suscripcionQueryService;
    }

    @PostMapping("/freemium")
    public ResponseEntity<SuscripcionResource> crearSuscripcionFreemium(@RequestBody CrearSuscripcionResource resource) {
        Optional<Suscripcion> suscripcion = suscripcionCommandService
                .handle(CrearSuscripcionCommandFromResourceAssembler.toCommandFromResource(resource), Plan.FREEMIUM);
        return suscripcion.map(source -> new ResponseEntity<>(SuscripcionResourceFromEntityAssembler.toResourceFromEntity(source), HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/premium")
    public ResponseEntity<SuscripcionResource> crearSuscripcionPremium(@RequestBody CrearSuscripcionResource resource) {
        Optional<Suscripcion> suscripcion = suscripcionCommandService
                .handle(CrearSuscripcionCommandFromResourceAssembler.toCommandFromResource(resource), Plan.PREMIUM);
        return suscripcion.map(source -> new ResponseEntity<>(SuscripcionResourceFromEntityAssembler.toResourceFromEntity(source), HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<SuscripcionResource> getSuscripcionByUsuarioId(@PathVariable Long usuarioId) {
        Optional<Suscripcion> suscripcion = suscripcionQueryService.getByUsuarioId(usuarioId);
        return suscripcion.map(source -> new ResponseEntity<>(SuscripcionResourceFromEntityAssembler.toResourceFromEntity(source), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/usuario/{usuarioId}")
    public ResponseEntity<SuscripcionResource> actualizarSuscripcion(
            @PathVariable Long usuarioId) {
        var command = ActualizarSuscripcionCommandFromResourceAssembler.toCommandFromResource(usuarioId);
        Optional<Suscripcion> suscripcion = suscripcionCommandService.handle(command);
        return suscripcion.map(source -> new ResponseEntity<>(SuscripcionResourceFromEntityAssembler.toResourceFromEntity(source), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/cambiar-plan/{usuarioId}")
    public ResponseEntity<SuscripcionResource> cambiarTipoPlanSuscripcion(
            @PathVariable Long usuarioId,
            @RequestBody ActualizarSuscripcionResource resource) {
        var command = new CambiarTipoPlanSuscripcionCommand(usuarioId, resource.tipo());
        Optional<Suscripcion> suscripcion = suscripcionCommandService.handle(command);
        return suscripcion.map(source -> new ResponseEntity<>(SuscripcionResourceFromEntityAssembler.toResourceFromEntity(source), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/usuario/{usuarioId}")
    public ResponseEntity<Object> eliminarSuscripcion(@PathVariable Long usuarioId) {
        Optional<Suscripcion> suscripcion = suscripcionCommandService.handle(new EliminarSuscripcionCommand(usuarioId));
        suscripcion.map(source -> new ResponseEntity<>(HttpStatus.NO_CONTENT))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        return ResponseEntity.ok("Suscripción eliminada correctamente");
    }
}