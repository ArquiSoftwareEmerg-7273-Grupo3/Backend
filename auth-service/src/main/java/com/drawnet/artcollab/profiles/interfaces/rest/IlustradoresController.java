package com.drawnet.artcollab.profiles.interfaces.rest;

import com.drawnet.artcollab.profiles.domain.services.IlustradorCommandService;
import com.drawnet.artcollab.profiles.infrastructure.persistence.jpa.repositories.IlustradorRepository;
import com.drawnet.artcollab.profiles.interfaces.rest.resources.CreateIlustradorResource;
import com.drawnet.artcollab.profiles.interfaces.rest.resources.IlustradorResource;
import com.drawnet.artcollab.profiles.interfaces.rest.transform.CreateIlustradorCommandFromResourceAssembler;
import com.drawnet.artcollab.profiles.interfaces.rest.transform.IlustradorResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/ilustradores", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Ilustradores", description = "Endpoints de Gestión de Ilustradores")
public class IlustradoresController {
    private final IlustradorCommandService ilustradorCommandService;

    public IlustradoresController(IlustradorCommandService ilustradorCommandService) {
        this.ilustradorCommandService = ilustradorCommandService;
    }

    @PostMapping
    public ResponseEntity<IlustradorResource> createPatient(@RequestBody CreateIlustradorResource resource) {
        var createIlustradorCommand = CreateIlustradorCommandFromResourceAssembler.toCommandFromResource(resource);
        var patient = ilustradorCommandService.handle(createIlustradorCommand);
        if (patient.isEmpty()) return ResponseEntity.badRequest().build();
        var ilustradorResource = IlustradorResourceFromEntityAssembler.toResourceFromEntity(patient.get());
        return new ResponseEntity<>(ilustradorResource, HttpStatus.CREATED);
    }
}
