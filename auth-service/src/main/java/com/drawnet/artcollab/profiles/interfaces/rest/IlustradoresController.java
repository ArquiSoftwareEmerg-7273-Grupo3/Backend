package com.drawnet.artcollab.profiles.interfaces.rest;

import com.drawnet.artcollab.profiles.domain.services.IlustradorCommandService;
import com.drawnet.artcollab.profiles.interfaces.rest.resources.CreateIlustradorResource;
import com.drawnet.artcollab.profiles.interfaces.rest.resources.IlustradorResource;
import com.drawnet.artcollab.profiles.interfaces.rest.transform.CreateIlustradorCommandFromResourceAssembler;
import com.drawnet.artcollab.profiles.interfaces.rest.transform.IlustradorResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<IlustradorResource> createIlustrador(
            @RequestBody CreateIlustradorResource resource,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // Obtener el ID del usuario autenticado
        // Nota: Necesitarás implementar un método para extraer el userId del UserDetails
        Long userId = extractUserIdFromUserDetails(userDetails);
        
        var createIlustradorCommand = CreateIlustradorCommandFromResourceAssembler.toCommandFromResource(resource, userId);
        var ilustrador = ilustradorCommandService.handle(createIlustradorCommand);
        if (ilustrador.isEmpty()) return ResponseEntity.badRequest().build();
        var ilustradorResource = IlustradorResourceFromEntityAssembler.toResourceFromEntity(ilustrador.get());
        return new ResponseEntity<>(ilustradorResource, HttpStatus.CREATED);
    }

    // Método auxiliar para extraer userId del UserDetails
    private Long extractUserIdFromUserDetails(UserDetails userDetails) {
        if (userDetails instanceof com.drawnet.artcollab.iam.infrastructure.authorization.sfs.model.UserDetailsImpl userDetailsImpl) {
            return userDetailsImpl.getUserId();
        }
        throw new IllegalArgumentException("No se pudo obtener el ID del usuario del token");
    }
}
