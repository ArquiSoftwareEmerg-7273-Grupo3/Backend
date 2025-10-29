package com.drawnet.artcollab.portafolioservice.infrastructure.external.clients;

import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.UserResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", path = "/api/v1/users")
public interface IlustradorCliente {
    @GetMapping("/{id}")
    //ResponseEntity<?> verificarUsuario(@PathVariable Long id);
    UserResource verificarUsuario(@PathVariable Long id);
}