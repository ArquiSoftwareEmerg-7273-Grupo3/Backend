package com.drawnet.artcollab.iam.interfaces.rest;

import com.drawnet.artcollab.iam.domain.model.aggregates.User;
import com.drawnet.artcollab.iam.domain.model.queries.GetAllUsersQuery;
import com.drawnet.artcollab.iam.domain.model.queries.GetUserByIdAndRolQuery;
import com.drawnet.artcollab.iam.domain.model.queries.GetUserByIdQuery;
import com.drawnet.artcollab.iam.domain.model.queries.GetUserByIdWithProfilesQuery;
import com.drawnet.artcollab.iam.domain.model.queries.SearchUsersByNameQuery;
import com.drawnet.artcollab.iam.domain.services.UserQueryService;
import com.drawnet.artcollab.iam.interfaces.rest.resources.UserResource;
import com.drawnet.artcollab.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "Endpoints de gestion de Usuarios")
public class UsersController {
    private final UserQueryService userQueryService;

    public UsersController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping
    public ResponseEntity<List<UserResource>> getAllUsers() {
        var getAllUsersQuery = new GetAllUsersQuery();
        var users = userQueryService.handle(getAllUsersQuery);
        var userResources = users.stream().map(UserResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(userResources);
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserResource> getUserById(@PathVariable Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    @GetMapping("/{id}/role/{role}")
    public ResponseEntity<UserResource> getUserByIdAndRole(@PathVariable Long id, @PathVariable String role) {
        //Optional<User> user = userQueryService.handle(new GetUserByIdAndRolQuery(id, role));
        //return user.map(u -> new UserResource(u.getId(), u.getUsername(), u.getRole().getStringName()))
        //        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        var getUserByIdAndRolQuery = new GetUserByIdAndRolQuery(id, role);
        var user = userQueryService.handle(getUserByIdAndRolQuery);
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    /**
     * Endpoint para obtener toda la información del usuario autenticado
     * Incluye datos básicos + perfiles de ilustrador/escritor si existen
     */
    @GetMapping("/me")
    public ResponseEntity<UserResource> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        // Extraer el userId del token JWT
        Long userId = extractUserIdFromUserDetails(userDetails);
        
        // Buscar el usuario con toda su información incluyendo perfiles
        var getUserWithProfilesQuery = new GetUserByIdWithProfilesQuery(userId);
        var user = userQueryService.handle(getUserWithProfilesQuery);
        
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        // Convertir a resource con toda la información
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    /**
     * Endpoint para buscar usuarios por nombre o apellido
     * GET /api/v1/users/search?q=searchTerm
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserResource>> searchUsers(@RequestParam("q") String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        var searchQuery = new SearchUsersByNameQuery(searchTerm.trim());
        var users = userQueryService.handle(searchQuery);
        var userResources = users.stream()
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        
        return ResponseEntity.ok(userResources);
    }

    // Método auxiliar para extraer userId del UserDetails
    private Long extractUserIdFromUserDetails(UserDetails userDetails) {
        if (userDetails instanceof com.drawnet.artcollab.iam.infrastructure.authorization.sfs.model.UserDetailsImpl userDetailsImpl) {
            return userDetailsImpl.getUserId();
        }
        throw new IllegalArgumentException("No se pudo obtener el ID del usuario del token");
    }



}
