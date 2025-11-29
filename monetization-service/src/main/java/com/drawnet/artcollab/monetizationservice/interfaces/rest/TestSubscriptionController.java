package com.drawnet.artcollab.monetizationservice.interfaces.rest;

import com.drawnet.artcollab.monetizationservice.application.service.MercadoPagoSubscriptionService;
import com.drawnet.artcollab.monetizationservice.interfaces.dto.SubscriptionResponse;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controlador para simular suscripciones en ambiente de prueba
 * SOLO PARA DESARROLLO - Eliminar en producción
 */
@RestController
@RequestMapping("/api/mercadopago/test")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
@RequiredArgsConstructor
public class TestSubscriptionController {
    
    private final MercadoPagoSubscriptionService subscriptionService;
    
    /**
     * Verificar si un plan existe en Mercado Pago
     */
    @GetMapping("/verify-plan/{planId}")
    public ResponseEntity<?> verifyPlan(@PathVariable String planId) {
        try {
            JsonObject plan = subscriptionService.getPlan(planId);
            
            System.out.println("✅ Plan encontrado: " + plan.toString());
            
            return ResponseEntity.ok(plan.toString());
        } catch (Exception e) {
            System.err.println("❌ Error al verificar plan: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Simular una suscripción exitosa para pruebas
     */
    @PostMapping("/simulate-subscription")
    public ResponseEntity<SubscriptionResponse> simulateSubscription(@RequestParam String email) {
        // Generar un ID de suscripción simulado
        String subscriptionId = "test_sub_" + UUID.randomUUID().toString().substring(0, 8);
        
        SubscriptionResponse response = SubscriptionResponse.builder()
            .subscriptionId(subscriptionId)
            .customerId("test_customer_123")
            .status("authorized")
            .message("Suscripción de prueba creada exitosamente")
            .build();
        
        System.out.println("✅ Suscripción de prueba creada: " + subscriptionId + " para " + email);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Verificar el estado de una suscripción de prueba
     */
    @GetMapping("/subscription/{subscriptionId}")
    public ResponseEntity<SubscriptionResponse> getTestSubscription(@PathVariable String subscriptionId) {
        SubscriptionResponse response = SubscriptionResponse.builder()
            .subscriptionId(subscriptionId)
            .customerId("test_customer_123")
            .status("authorized")
            .message("Suscripción de prueba activa")
            .build();
        
        return ResponseEntity.ok(response);
    }
}
