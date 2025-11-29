package com.drawnet.artcollab.monetizationservice.application.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MercadoPagoSubscriptionService {
    
    private final WebClient mercadoPagoWebClient;
    private final Gson gson = new Gson();
    
    /**
     * Crear un plan de suscripción mensual
     * @param reason Nombre del plan (ej: "Plan Premium")
     * @param amount Monto mensual (ej: 50.00 soles)
     * @param backUrl URL de retorno
     */
    public JsonObject createPlan(String reason, BigDecimal amount, String backUrl) {
        try {
            Map<String, Object> autoRecurring = new HashMap<>();
            autoRecurring.put("frequency", 1);
            autoRecurring.put("frequency_type", "months");
            autoRecurring.put("transaction_amount", amount);
            autoRecurring.put("currency_id", "PEN");
            
            Map<String, Object> plan = new HashMap<>();
            plan.put("reason", reason);
            plan.put("auto_recurring", autoRecurring);
            plan.put("back_url", backUrl);
            
            System.out.println("=== CREANDO PLAN EN MERCADO PAGO ===");
            System.out.println("Datos del plan: " + gson.toJson(plan));
            System.out.println("URL: https://api.mercadopago.com/preapproval_plan");
            
            String response = mercadoPagoWebClient.post()
                .uri("/preapproval_plan")
                .bodyValue(plan)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                    clientResponse -> clientResponse.bodyToMono(String.class)
                        .map(body -> {
                            System.err.println("=== ERROR DE MERCADO PAGO ===");
                            System.err.println("Status: " + clientResponse.statusCode());
                            System.err.println("Body: " + body);
                            return new RuntimeException("Error de Mercado Pago [" + clientResponse.statusCode() + "]: " + body);
                        }))
                .bodyToMono(String.class)
                .block();
            
            System.out.println("=== RESPUESTA EXITOSA ===");
            System.out.println("Respuesta: " + response);
            return gson.fromJson(response, JsonObject.class);
        } catch (Exception e) {
            System.err.println("=== EXCEPCIÓN AL CREAR PLAN ===");
            System.err.println("Tipo: " + e.getClass().getName());
            System.err.println("Mensaje: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al crear plan: " + e.getMessage(), e);
        }
    }
    
    /**
     * Crear una suscripción asociada a un plan
     * @param preapprovalPlanId ID del plan creado previamente
     * @param cardTokenId Token de la tarjeta del cliente
     * @param email Email del cliente
     * @param backUrl URL de retorno
     */
    public JsonObject createSubscription(String preapprovalPlanId, String cardTokenId, 
                                        String email, String backUrl) {
        Map<String, Object> subscription = new HashMap<>();
        subscription.put("preapproval_plan_id", preapprovalPlanId);
        subscription.put("card_token_id", cardTokenId);
        subscription.put("payer_email", email);
        subscription.put("back_url", backUrl);
        subscription.put("status", "authorized");
        
        String response = mercadoPagoWebClient.post()
            .uri("/preapproval")
            .bodyValue(subscription)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        
        return gson.fromJson(response, JsonObject.class);
    }
    
    /**
     * Obtener información de una suscripción
     */
    public JsonObject getSubscription(String subscriptionId) {
        String response = mercadoPagoWebClient.get()
            .uri("/preapproval/" + subscriptionId)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        
        return gson.fromJson(response, JsonObject.class);
    }
    
    /**
     * Cancelar una suscripción
     */
    public JsonObject cancelSubscription(String subscriptionId) {
        Map<String, Object> update = new HashMap<>();
        update.put("status", "cancelled");
        
        String response = mercadoPagoWebClient.put()
            .uri("/preapproval/" + subscriptionId)
            .bodyValue(update)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        
        return gson.fromJson(response, JsonObject.class);
    }
    
    /**
     * Pausar una suscripción
     */
    public JsonObject pauseSubscription(String subscriptionId) {
        Map<String, Object> update = new HashMap<>();
        update.put("status", "paused");
        
        String response = mercadoPagoWebClient.put()
            .uri("/preapproval/" + subscriptionId)
            .bodyValue(update)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        
        return gson.fromJson(response, JsonObject.class);
    }
    
    /**
     * Obtener información de un plan
     */
    public JsonObject getPlan(String planId) {
        String response = mercadoPagoWebClient.get()
            .uri("/preapproval_plan/" + planId)
            .retrieve()
            .bodyToMono(String.class)
            .block();
        
        return gson.fromJson(response, JsonObject.class);
    }
}
