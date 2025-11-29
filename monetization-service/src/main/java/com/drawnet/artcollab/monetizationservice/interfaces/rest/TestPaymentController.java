package com.drawnet.artcollab.monetizationservice.interfaces.rest;

import com.drawnet.artcollab.monetizationservice.application.service.UserSubscriptionManagementService;
import com.drawnet.artcollab.monetizationservice.domain.model.UserSubscription;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para simular pagos en desarrollo
 * SOLO PARA TESTING - ELIMINAR EN PRODUCCIÓN
 */
@RestController
@RequestMapping("/api/test/payments")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
@RequiredArgsConstructor
public class TestPaymentController {
    
    private final UserSubscriptionManagementService subscriptionService;
    
    /**
     * Simular un pago exitoso y activar suscripción
     * Esto evita los problemas con las tarjetas de prueba de Mercado Pago
     */
    @PostMapping("/simulate-success")
    public ResponseEntity<Map<String, Object>> simulateSuccessfulPayment(@RequestBody Map<String, String> request) {
        try {
            String userId = request.get("userId");
            String userEmail = request.get("userEmail");
            String userType = request.get("userType");
            
            System.out.println("=== SIMULANDO PAGO EXITOSO ===");
            System.out.println("User ID: " + userId);
            System.out.println("Email: " + userEmail);
            System.out.println("Tipo: " + userType);
            
            // Activar suscripción directamente
            UserSubscription subscription = subscriptionService.activateSubscription(
                userId,
                userEmail,
                userType,
                "test_subscription_" + System.currentTimeMillis(),
                "test_plan_123"
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pago simulado exitosamente - Suscripción activada");
            response.put("subscriptionId", subscription.getId());
            response.put("userId", userId);
            response.put("status", subscription.getStatus());
            response.put("isActive", subscription.getIsActive());
            
            System.out.println("Suscripción activada exitosamente");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error al simular pago: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al simular pago: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
    
    /**
     * Verificar estado de suscripción
     */
    @GetMapping("/subscription/{userId}")
    public ResponseEntity<Map<String, Object>> checkSubscription(@PathVariable String userId) {
        try {
            boolean hasSubscription = subscriptionService.hasActiveSubscription(userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("userId", userId);
            response.put("hasActiveSubscription", hasSubscription);
            
            subscriptionService.getUserSubscription(userId).ifPresent(sub -> {
                response.put("status", sub.getStatus());
                response.put("startDate", sub.getStartDate());
                response.put("userType", sub.getUserType());
            });
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
