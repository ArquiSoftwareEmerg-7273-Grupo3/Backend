package com.drawnet.artcollab.profiles.interfaces.rest;


import com.drawnet.artcollab.profiles.domain.services.EscritorCommandService;
import com.drawnet.artcollab.profiles.infrastructure.persistence.jpa.repositories.EscritorRepository;
import com.drawnet.artcollab.profiles.interfaces.rest.resources.CreateEscritorResource;
import com.drawnet.artcollab.profiles.interfaces.rest.resources.EscritorResource;
import com.drawnet.artcollab.profiles.interfaces.rest.transform.CreateEscritorCommandFromResourceAssembler;
import com.drawnet.artcollab.profiles.interfaces.rest.transform.EscritorResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/escritores", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Escritores", description = "Endpoints de Gestión de Escritores")
public class EscritorController {

    private final EscritorCommandService escritorCommandService;

    public EscritorController(EscritorCommandService escritorCommandService) {
        this.escritorCommandService = escritorCommandService;
    }

    @PostMapping
    public ResponseEntity<EscritorResource> createDoctor(@RequestBody CreateEscritorResource resource) {
        var createEscritorCommand = CreateEscritorCommandFromResourceAssembler.toCommandFromResource(resource);
        var doctor = escritorCommandService.handle(createEscritorCommand);
        if (doctor.isEmpty()) return ResponseEntity.badRequest().build();
        var esccritorResource = EscritorResourceFromEntityAssembler.toResourceFromEntity(doctor.get());
        return new ResponseEntity<>(esccritorResource, HttpStatus.CREATED);

    }
}
