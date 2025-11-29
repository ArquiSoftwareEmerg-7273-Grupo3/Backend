package com.drawnet.artcollab.profiles.interfaces.rest;

import com.drawnet.artcollab.profiles.domain.model.aggregates.Escritor;
import com.drawnet.artcollab.profiles.domain.model.aggregates.Ilustrador;
import com.drawnet.artcollab.profiles.infrastructure.persistence.jpa.repositories.EscritorRepository;
import com.drawnet.artcollab.profiles.infrastructure.persistence.jpa.repositories.IlustradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/profiles/subscription")
@RequiredArgsConstructor
public class SubscriptionController {
    
    private final IlustradorRepository ilustradorRepository;
    private final EscritorRepository escritorRepository;
    
    /**
     * Activar suscripción para un usuario (Ilustrador o Escritor)
     */
    @PostMapping("/activate/{userId}")
    public ResponseEntity<?> activateSubscription(
            @PathVariable Long userId,
            @RequestParam String userType) {
        
        try {
            if ("ILUSTRADOR".equalsIgnoreCase(userType)) {
                Ilustrador ilustrador = ilustradorRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("Ilustrador no encontrado"));
                
                ilustrador.activarSubscripcion();
                ilustradorRepository.save(ilustrador);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Suscripción activada para ilustrador");
                response.put("userId", userId);
                response.put("subscription", true);
                
                return ResponseEntity.ok(response);
                
            } else if ("ESCRITOR".equalsIgnoreCase(userType)) {
                Escritor escritor = escritorRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("Escritor no encontrado"));
                
                escritor.activarSubscripcion();
                escritorRepository.save(escritor);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Suscripción activada para escritor");
                response.put("userId", userId);
                response.put("subscription", true);
                
                return ResponseEntity.ok(response);
            } else {
                throw new RuntimeException("Tipo de usuario inválido");
            }
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al activar suscripción: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Desactivar suscripción
     */
    @PostMapping("/deactivate/{userId}")
    public ResponseEntity<?> deactivateSubscription(
            @PathVariable Long userId,
            @RequestParam String userType) {
        
        try {
            if ("ILUSTRADOR".equalsIgnoreCase(userType)) {
                Ilustrador ilustrador = ilustradorRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("Ilustrador no encontrado"));
                
                ilustrador.desactivarSubscripcion();
                ilustradorRepository.save(ilustrador);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Suscripción desactivada para ilustrador");
                response.put("subscription", false);
                
                return ResponseEntity.ok(response);
                
            } else if ("ESCRITOR".equalsIgnoreCase(userType)) {
                Escritor escritor = escritorRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("Escritor no encontrado"));
                
                escritor.desactivarSubscripcion();
                escritorRepository.save(escritor);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Suscripción desactivada para escritor");
                response.put("subscription", false);
                
                return ResponseEntity.ok(response);
            } else {
                throw new RuntimeException("Tipo de usuario inválido");
            }
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al desactivar suscripción: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Verificar estado de suscripción
     */
    @GetMapping("/check/{userId}")
    public ResponseEntity<?> checkSubscription(
            @PathVariable Long userId,
            @RequestParam String userType) {
        
        try {
            Boolean hasSubscription = false;
            
            if ("ILUSTRADOR".equalsIgnoreCase(userType)) {
                Ilustrador ilustrador = ilustradorRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("Ilustrador no encontrado"));
                hasSubscription = ilustrador.getSubscripcion();
                
            } else if ("ESCRITOR".equalsIgnoreCase(userType)) {
                Escritor escritor = escritorRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("Escritor no encontrado"));
                hasSubscription = escritor.getSubscripcion();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("userType", userType);
            response.put("subscription", hasSubscription);
            response.put("hasActiveSubscription", hasSubscription);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al verificar suscripción: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
