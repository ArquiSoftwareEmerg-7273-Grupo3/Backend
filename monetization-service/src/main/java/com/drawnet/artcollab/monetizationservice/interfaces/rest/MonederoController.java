package com.drawnet.artcollab.monetizationservice.interfaces.rest;

import com.drawnet.artcollab.monetizationservice.application.internal.commandservices.MonederoCommandServiceImpl;
import com.drawnet.artcollab.monetizationservice.application.internal.queryservices.MonederoQueryServiceImpl;
import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Monedero;
import com.drawnet.artcollab.monetizationservice.interfaces.rest.resources.*;
import com.drawnet.artcollab.monetizationservice.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/monederos", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Monederos", description = "Operaciones relacionadas con monedero")
public class MonederoController {

    private final MonederoQueryServiceImpl monederoQueryServiceImpl;
    private final MonederoCommandServiceImpl monederoCommandServiceImpl;

    public MonederoController(MonederoQueryServiceImpl monederoQueryServiceImpl,
                              MonederoCommandServiceImpl monederoCommandServiceImpl) {
        this.monederoQueryServiceImpl = monederoQueryServiceImpl;
        this.monederoCommandServiceImpl = monederoCommandServiceImpl;
    }

    @PostMapping
    public ResponseEntity<MonederoResource> crearMonedero(@RequestBody CrearMonederoResource resource) {
        Optional<Monedero> monedero = monederoCommandServiceImpl
                .handle(CrearMonederoCommandFromResourceAssembler.toCommandFromResource(resource.usuarioId()));
        return monedero.map(source -> new ResponseEntity<>(MonederoResourceFromEntityAssembler.toResourceFromEntity(source), HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<MonederoResource> getMonederoByUsuarioId(@PathVariable Long usuarioId) {
        Optional<Monedero> suscripcion = monederoQueryServiceImpl.getByUsuarioId(usuarioId);
        return suscripcion.map(source -> new ResponseEntity<>(MonederoResourceFromEntityAssembler.toResourceFromEntity(source), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/usuario/{usuarioId}/saldo")
    public ResponseEntity<MonederoResource> actualizarMontoMonedero(
            @PathVariable Long usuarioId,
            @RequestBody ActualizarMonederoResource resource) {
        var command = ActualizarMonederoCommandFromResourceAssembler.toCommandFromResource(usuarioId, resource);
        Optional<Monedero> monedero = monederoCommandServiceImpl.handle(command);
        return monedero.map(source -> new ResponseEntity<>(MonederoResourceFromEntityAssembler.toResourceFromEntity(source), HttpStatus.OK))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}