package com.drawnet.artcollab.monetizationservice.interfaces.rest;

import com.drawnet.artcollab.monetizationservice.application.service.MercadoPagoSubscriptionService;
import com.drawnet.artcollab.monetizationservice.interfaces.dto.*;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/mercadopago/subscriptions")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
@RequiredArgsConstructor
public class MercadoPagoSubscriptionController {
    
    private final MercadoPagoSubscriptionService subscriptionService;
    
    /**
     * Crear un plan de suscripción mensual de 50 soles
     */
    @PostMapping("/plans")
    public ResponseEntity<PlanResponse> createPlan(@RequestBody PlanRequest request) {
        try {
            System.out.println("Recibida petición para crear plan: " + request);
            
            // Por defecto: Plan mensual de 2 soles (mínimo requerido por Mercado Pago en Perú)
            BigDecimal amount = request.getAutoRecurringAmount() != null 
                ? request.getAutoRecurringAmount() 
                : new BigDecimal("2.00");
            
            String reason = request.getReason() != null 
                ? request.getReason() 
                : "Plan Premium Mensual";
            
            String backUrl = request.getBackUrl() != null 
                ? request.getBackUrl() 
                : "https://tu-dominio.com/subscription/success";
            
            JsonObject plan = subscriptionService.createPlan(reason, amount, backUrl);
            
            if (plan == null || !plan.has("id")) {
                throw new RuntimeException("Respuesta inválida de Mercado Pago");
            }
            
            JsonObject autoRecurring = plan.getAsJsonObject("auto_recurring");
            
            PlanResponse response = PlanResponse.builder()
                .planId(plan.get("id").getAsString())
                .reason(plan.get("reason").getAsString())
                .autoRecurringAmount(autoRecurring.get("transaction_amount").getAsBigDecimal())
                .frequency(autoRecurring.get("frequency").getAsInt() + " " + 
                          autoRecurring.get("frequency_type").getAsString())
                .status(plan.get("status").getAsString())
                .initPoint(plan.get("init_point").getAsString())
                .message("Plan creado exitosamente")
                .build();
            
            System.out.println("Plan creado exitosamente: " + response.getPlanId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error al crear plan: " + e.getMessage());
            e.printStackTrace();
            
            PlanResponse errorResponse = PlanResponse.builder()
                .message("Error al crear plan: " + e.getMessage())
                .build();
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * Obtener información de un plan
     */
    @GetMapping("/plans/{planId}")
    public ResponseEntity<PlanResponse> getPlan(@PathVariable String planId) {
        try {
            JsonObject plan = subscriptionService.getPlan(planId);
            
            JsonObject autoRecurring = plan.getAsJsonObject("auto_recurring");
            
            PlanResponse response = PlanResponse.builder()
                .planId(plan.get("id").getAsString())
                .reason(plan.get("reason").getAsString())
                .autoRecurringAmount(autoRecurring.get("transaction_amount").getAsBigDecimal())
                .frequency(autoRecurring.get("frequency").getAsInt() + " " + 
                          autoRecurring.get("frequency_type").getAsString())
                .status(plan.get("status").getAsString())
                .initPoint(plan.get("init_point").getAsString())
                .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            PlanResponse errorResponse = PlanResponse.builder()
                .message("Error al obtener plan: " + e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Crear una suscripción asociada a un plan
     */
    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscription(@RequestBody SubscriptionRequest request) {
        try {
            String backUrl = request.getBackUrl() != null 
                ? request.getBackUrl() 
                : "https://tu-dominio.com/subscription/success";
            
            JsonObject subscription = subscriptionService.createSubscription(
                request.getPreapprovalPlanId(),
                request.getCardTokenId(),
                request.getEmail(),
                backUrl
            );
            
            SubscriptionResponse response = SubscriptionResponse.builder()
                .subscriptionId(subscription.get("id").getAsString())
                .status(subscription.get("status").getAsString())
                .message("Suscripción creada exitosamente")
                .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            SubscriptionResponse errorResponse = SubscriptionResponse.builder()
                .message("Error al crear suscripción: " + e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Obtener información de una suscripción
     */
    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponse> getSubscription(@PathVariable String subscriptionId) {
        try {
            JsonObject subscription = subscriptionService.getSubscription(subscriptionId);
            
            SubscriptionResponse response = SubscriptionResponse.builder()
                .subscriptionId(subscription.get("id").getAsString())
                .status(subscription.get("status").getAsString())
                .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            SubscriptionResponse errorResponse = SubscriptionResponse.builder()
                .message("Error al obtener suscripción: " + e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Cancelar una suscripción
     */
    @PostMapping("/{subscriptionId}/cancel")
    public ResponseEntity<SubscriptionResponse> cancelSubscription(@PathVariable String subscriptionId) {
        try {
            JsonObject subscription = subscriptionService.cancelSubscription(subscriptionId);
            
            SubscriptionResponse response = SubscriptionResponse.builder()
                .subscriptionId(subscription.get("id").getAsString())
                .status(subscription.get("status").getAsString())
                .message("Suscripción cancelada exitosamente")
                .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            SubscriptionResponse errorResponse = SubscriptionResponse.builder()
                .message("Error al cancelar suscripción: " + e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Pausar una suscripción
     */
    @PostMapping("/{subscriptionId}/pause")
    public ResponseEntity<SubscriptionResponse> pauseSubscription(@PathVariable String subscriptionId) {
        try {
            JsonObject subscription = subscriptionService.pauseSubscription(subscriptionId);
            
            SubscriptionResponse response = SubscriptionResponse.builder()
                .subscriptionId(subscription.get("id").getAsString())
                .status(subscription.get("status").getAsString())
                .message("Suscripción pausada exitosamente")
                .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            SubscriptionResponse errorResponse = SubscriptionResponse.builder()
                .message("Error al pausar suscripción: " + e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Webhook para recibir notificaciones de suscripciones
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                               @RequestParam(required = false) String type,
                                               @RequestParam(required = false) String id) {
        // Tipos de notificaciones para suscripciones:
        // - preapproval: Cambios en la suscripción
        // - authorized_payment: Pago autorizado
        
        System.out.println("Webhook recibido - Type: " + type + ", ID: " + id);
        System.out.println("Payload: " + payload);
        
        return ResponseEntity.ok("Webhook procesado");
    }
}
