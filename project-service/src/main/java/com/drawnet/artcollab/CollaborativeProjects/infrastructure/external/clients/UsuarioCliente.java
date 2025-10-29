package com.drawnet.artcollab.CollaborativeProjects.infrastructure.external.clients;

import com.drawnet.artcollab.CollaborativeProjects.interfaces.rest.resources.UserResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", path = "/api/v1/users")

public interface UsuarioCliente {
    @GetMapping("/{id}")
    UserResource verificarUsuario(@PathVariable Long id);
}
