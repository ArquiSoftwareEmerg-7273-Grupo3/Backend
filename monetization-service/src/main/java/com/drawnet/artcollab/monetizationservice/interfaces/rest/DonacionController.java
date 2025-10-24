package com.drawnet.artcollab.monetizationservice.interfaces.rest;

import com.drawnet.artcollab.monetizationservice.application.internal.commandservices.DonacionCommandServiceImpl;
import com.drawnet.artcollab.monetizationservice.application.internal.commandservices.MonederoCommandServiceImpl;
import com.drawnet.artcollab.monetizationservice.application.internal.queryservices.DonacionQueryServiceImpl;
import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.Donacion;
import com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.repositories.MonederoRepository;
import com.drawnet.artcollab.monetizationservice.interfaces.rest.resources.CrearDonacionResource;
import com.drawnet.artcollab.monetizationservice.interfaces.rest.resources.DonacionResource;
import com.drawnet.artcollab.monetizationservice.interfaces.rest.transform.CrearDonacionCommandFromResourceAssembler;
import com.drawnet.artcollab.monetizationservice.interfaces.rest.transform.DonacionResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/donaciones", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Donación", description = "Operaciones relacionadas con donaciones")
public class DonacionController {

    private final DonacionCommandServiceImpl donacionCommandService;
    private final DonacionQueryServiceImpl donacionQueryService;
    private final MonederoCommandServiceImpl monederoCommandService;
    private final MonederoRepository monederoRepository;

    public DonacionController(DonacionCommandServiceImpl donacionCommandService, DonacionQueryServiceImpl donacionQueryService,
                              MonederoCommandServiceImpl monederoCommandService, MonederoRepository monederoRepository) {
        this.donacionCommandService = donacionCommandService;
        this.donacionQueryService = donacionQueryService;
        this.monederoCommandService = monederoCommandService;
        this.monederoRepository = monederoRepository;

    }

    @PostMapping
    public ResponseEntity<DonacionResource> crearDonacion(@RequestBody CrearDonacionResource resource) {
        var command = CrearDonacionCommandFromResourceAssembler.toCommandFromResource(resource);

        var donanteOpt = monederoRepository.findByUsuarioId(command.donanteId());
        var receptorOpt = monederoRepository.findByUsuarioId(command.receptorId());

        if (donanteOpt.isEmpty() || receptorOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Donacion donacion = monederoCommandService.donar(donanteOpt.get(), receptorOpt.get(), command);
            DonacionResource donacionResource = DonacionResourceFromEntityAssembler.toResourceFromEntity(donacion);
            return new ResponseEntity<>(donacionResource, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<DonacionResource>> getDonacionById(@PathVariable Long id) {
        Optional<Donacion> donacion = donacionQueryService.getById(id);
        return ResponseEntity.ok(donacion.stream()
                .map(DonacionResourceFromEntityAssembler::toResourceFromEntity)
                .collect(Collectors.toList()));
    }

    @GetMapping("/donante/{donanteId}")
    public ResponseEntity<Object> getDonacionesByDonanteId(@PathVariable Long donanteId) {
        List<Donacion> donaciones = donacionQueryService.getByDonanteId(donanteId);
        if (donaciones.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Object() {
                        public final String mensaje = "No se encontraron donaciones para el donante especificado";
                        public final List<DonacionResource> donaciones = List.of();
                    });
        }
        else {
            return ResponseEntity.ok(donaciones.stream()
                    .map(DonacionResourceFromEntityAssembler::toResourceFromEntity)
                    .collect(Collectors.toList()));
        }
    }

    @GetMapping("/receptor/{receptorId}")
    public ResponseEntity<Object> getDonacionesByReceptorId(@PathVariable Long receptorId) {
        List<Donacion> donaciones = donacionQueryService.getByReceptorId(receptorId);
        if (donaciones.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Object() {
                        public final String mensaje = "No se encontraron donaciones para el receptor especificado";
                        public final List<DonacionResource> donaciones = List.of();
                    });
        }
        else {
            return ResponseEntity.ok(donaciones.stream()
                    .map(DonacionResourceFromEntityAssembler::toResourceFromEntity)
                    .collect(Collectors.toList()));
        }
    }
}