package com.drawnet.artcollab.monetizationservice.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Cliente Feign para comunicarse con auth-service
 */
@FeignClient(name = "auth-service", path = "/api/profiles/subscription")
public interface AuthServiceClient {
    
    /**
     * Activar suscripción en auth-service
     */
    @PostMapping("/activate/{userId}")
    ResponseEntity<Map<String, Object>> activateSubscription(
            @PathVariable("userId") Long userId,
            @RequestParam("userType") String userType
    );
    
    /**
     * Desactivar suscripción en auth-service
     */
    @PostMapping("/deactivate/{userId}")
    ResponseEntity<Map<String, Object>> deactivateSubscription(
            @PathVariable("userId") Long userId,
            @RequestParam("userType") String userType
    );
}
