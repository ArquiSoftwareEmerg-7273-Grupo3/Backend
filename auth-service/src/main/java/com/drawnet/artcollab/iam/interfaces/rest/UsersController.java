package com.drawnet.artcollab.iam.interfaces.rest;

import com.drawnet.artcollab.iam.domain.model.aggregates.User;
import com.drawnet.artcollab.iam.domain.model.queries.GetAllUsersQuery;
import com.drawnet.artcollab.iam.domain.model.queries.GetUserByIdAndRolQuery;
import com.drawnet.artcollab.iam.domain.model.queries.GetUserByIdQuery;
import com.drawnet.artcollab.iam.domain.services.UserQueryService;
import com.drawnet.artcollab.iam.interfaces.rest.resources.UserResource;
import com.drawnet.artcollab.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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



}
