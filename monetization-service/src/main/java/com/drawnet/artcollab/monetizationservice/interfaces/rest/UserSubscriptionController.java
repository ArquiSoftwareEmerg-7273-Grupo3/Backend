package com.drawnet.artcollab.monetizationservice.interfaces.rest;

import com.drawnet.artcollab.monetizationservice.application.service.UserSubscriptionManagementService;
import com.drawnet.artcollab.monetizationservice.domain.model.UserSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user-subscriptions")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
@RequiredArgsConstructor
public class UserSubscriptionController {
    
    private final UserSubscriptionManagementService subscriptionService;
    
    /**
     * Activar suscripción para un usuario
     */
    @PostMapping("/activate")
    public ResponseEntity<?> activateSubscription(@RequestBody Map<String, String> request) {
        try {
            String userId = request.get("userId");
            String userEmail = request.get("userEmail");
            String userType = request.get("userType");  // "ILUSTRADOR" o "ESCRITOR"
            String mercadoPagoSubscriptionId = request.get("mercadoPagoSubscriptionId");
            String mercadoPagoPlanId = request.get("mercadoPagoPlanId");
            
            UserSubscription subscription = subscriptionService.activateSubscription(
                    userId, userEmail, userType, mercadoPagoSubscriptionId, mercadoPagoPlanId
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Suscripción activada exitosamente");
            response.put("subscription", subscription);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al activar suscripción: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    /**
     * Verificar si un usuario tiene suscripción activa
     */
    @GetMapping("/check/{userId}")
    public ResponseEntity<?> checkSubscription(@PathVariable String userId) {
        boolean hasSubscription = subscriptionService.hasActiveSubscription(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", userId);
        response.put("hasActiveSubscription", hasSubscription);
        response.put("subscription", hasSubscription);  // Para compatibilidad con frontend
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener detalles de la suscripción de un usuario
     */
    @GetMapping("/details/{userId}")
    public ResponseEntity<?> getSubscriptionDetails(@PathVariable String userId) {
        return subscriptionService.getUserSubscription(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Cancelar suscripción
     */
    @PostMapping("/cancel/{userId}")
    public ResponseEntity<?> cancelSubscription(@PathVariable String userId) {
        try {
            UserSubscription subscription = subscriptionService.cancelSubscription(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Suscripción cancelada exitosamente");
            response.put("subscription", subscription);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al cancelar suscripción: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
