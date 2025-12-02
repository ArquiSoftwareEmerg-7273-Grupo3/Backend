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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/escritores", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Escritores", description = "Endpoints de Gestión de Escritores")
public class EscritorController {

    private final EscritorCommandService escritorCommandService;
    private final EscritorRepository escritorRepository;

    public EscritorController(EscritorCommandService escritorCommandService, EscritorRepository escritorRepository) {
        this.escritorCommandService = escritorCommandService;
        this.escritorRepository = escritorRepository;
    }

    @PostMapping
    public ResponseEntity<EscritorResource> createEscritor(
            @RequestBody CreateEscritorResource resource,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // Obtener el ID del usuario autenticado
        Long userId = extractUserIdFromUserDetails(userDetails);
        
        var createEscritorCommand = CreateEscritorCommandFromResourceAssembler.toCommandFromResource(resource, userId);
        var escritor = escritorCommandService.handle(createEscritorCommand);
        if (escritor.isEmpty()) return ResponseEntity.badRequest().build();
        var escritorResource = EscritorResourceFromEntityAssembler.toResourceFromEntity(escritor.get());
        return new ResponseEntity<>(escritorResource, HttpStatus.CREATED);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<EscritorResource> getEscritorByUserId(@PathVariable Long userId) {
        var escritor = escritorRepository.findByUserId(userId);
        if (escritor.isEmpty()) return ResponseEntity.notFound().build();
        var escritorResource = EscritorResourceFromEntityAssembler.toResourceFromEntity(escritor.get());
        return ResponseEntity.ok(escritorResource);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EscritorResource> getEscritorById(@PathVariable Long id) {
        var escritor = escritorRepository.findById(id);
        if (escritor.isEmpty()) return ResponseEntity.notFound().build();
        var escritorResource = EscritorResourceFromEntityAssembler.toResourceFromEntity(escritor.get());
        return ResponseEntity.ok(escritorResource);
    }

    // Método auxiliar para extraer userId del UserDetails
    private Long extractUserIdFromUserDetails(UserDetails userDetails) {
        if (userDetails instanceof com.drawnet.artcollab.iam.infrastructure.authorization.sfs.model.UserDetailsImpl userDetailsImpl) {
            return userDetailsImpl.getUserId();
        }
        throw new IllegalArgumentException("No se pudo obtener el ID del usuario del token");
    }
}
