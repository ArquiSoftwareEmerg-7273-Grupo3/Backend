package com.drawnet.artcolab.libroservice.infrastructure.external.clients;

import com.drawnet.artcolab.libroservice.interfaces.rest.resources.UserResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", path = "/api/v1/users")
public interface EscritorCliente {
    @GetMapping("/{id}")
        //ResponseEntity<?> verificarUsuario(@PathVariable Long id);
    UserResource verificarUsuario(@PathVariable Long id);
}
