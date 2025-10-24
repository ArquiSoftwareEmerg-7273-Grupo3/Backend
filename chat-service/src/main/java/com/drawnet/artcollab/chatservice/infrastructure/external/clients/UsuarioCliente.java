package com.drawnet.artcollab.chatservice.infrastructure.external.clients;

import com.drawnet.artcollab.chatservice.interfaces.rest.resources.ExternalUserResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", path = "/api/v1/users")
public interface UsuarioCliente {
    @GetMapping("/{id}")
    ExternalUserResource getUsuarioById(@PathVariable Long id);
}