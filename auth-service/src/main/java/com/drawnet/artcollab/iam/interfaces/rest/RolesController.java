package com.drawnet.artcollab.iam.interfaces.rest;

import com.drawnet.artcollab.iam.domain.model.aggregates.User;
import com.drawnet.artcollab.iam.domain.model.queries.GetAllRolesQuery;
import com.drawnet.artcollab.iam.domain.model.queries.GetUserByIdAndRolQuery;
import com.drawnet.artcollab.iam.domain.model.valueobjects.Roles;
import com.drawnet.artcollab.iam.domain.services.RoleQueryService;
import com.drawnet.artcollab.iam.domain.services.UserQueryService;
import com.drawnet.artcollab.iam.interfaces.rest.resources.RoleResource;
import com.drawnet.artcollab.iam.interfaces.rest.resources.UserResource;
import com.drawnet.artcollab.iam.interfaces.rest.transform.RoleResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v1/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Roles", description = "Endpoints de gestion de Roles")
public class RolesController {
    private final RoleQueryService roleQueryService;

    public RolesController(RoleQueryService roleQueryService) {
        this.roleQueryService = roleQueryService;
    }

    @GetMapping
    public ResponseEntity<List<RoleResource>> getAllRoles() {
        var getAllRolesQuery = new GetAllRolesQuery();
        var roles = roleQueryService.handle(getAllRolesQuery);
        var roleResources = roles.stream().map(RoleResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(roleResources);
    }




}
