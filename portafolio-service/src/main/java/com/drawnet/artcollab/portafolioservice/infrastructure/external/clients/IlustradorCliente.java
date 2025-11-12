package com.drawnet.artcollab.portafolioservice.infrastructure.external.clients;

import com.drawnet.artcollab.portafolioservice.interfaces.rest.resources.UserResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service", path = "/api/v1")
public interface IlustradorCliente {
    
    /**
     * Verifica si un usuario existe por su ID
     */
    @GetMapping("/users/{id}")
    UserResource verificarUsuario(@PathVariable Long id);
    
    /**
     * Obtiene el ilustrador por userId.
     * IMPORTANTE: userId es el ID del User (tabla users),
     * retorna el perfil de Ilustrador con su ID (tabla ilustrador).
     */
    @GetMapping("/ilustradores/by-user/{userId}")
    IlustradorResource obtenerIlustradorPorUserId(@PathVariable Long userId);
}