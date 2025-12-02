package com.drawnet.artcollab.monetizationservice.interfaces.rest;

import com.drawnet.artcollab.monetizationservice.application.service.MercadoPagoService;
import com.drawnet.artcollab.monetizationservice.interfaces.dto.*;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mercadopago")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
@RequiredArgsConstructor
public class MercadoPagoController {
    
    private final MercadoPagoService mercadoPagoService;
    private final com.drawnet.artcollab.monetizationservice.application.service.SubscriptionActivationService subscriptionActivationService;
    
    /**
     * Crear un pago directo (requiere tokenización previa en frontend)
     */
    @PostMapping("/payments")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        try {
            Payment payment = mercadoPagoService.createPayment(
                request.getAmount(),
                request.getDescription(),
                request.getPaymentMethodId(),
                request.getEmail(),
                request.getToken(),
                request.getInstallments()
            );
            
            PaymentResponse response = PaymentResponse.builder()
                .paymentId(payment.getId())
                .status(payment.getStatus())
                .statusDetail(payment.getStatusDetail())
                .amount(payment.getTransactionAmount())
                .currency(payment.getCurrencyId())
                .message("Pago procesado exitosamente")
                .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            PaymentResponse errorResponse = PaymentResponse.builder()
                .message("Error al procesar pago: " + e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Obtener información de un pago
     */
    @GetMapping("/payments/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable Long paymentId) {
        try {
            Payment payment = mercadoPagoService.getPayment(paymentId);
            
            PaymentResponse response = PaymentResponse.builder()
                .paymentId(payment.getId())
                .status(payment.getStatus())
                .statusDetail(payment.getStatusDetail())
                .amount(payment.getTransactionAmount())
                .currency(payment.getCurrencyId())
                .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            PaymentResponse errorResponse = PaymentResponse.builder()
                .message("Error al obtener pago: " + e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Crear una preferencia de pago (Checkout Pro)
     * Retorna un link donde el usuario puede pagar
     */
    @PostMapping("/preferences")
    public ResponseEntity<PreferenceResponse> createPreference(@RequestBody PreferenceRequest request) {
        try {
            System.out.println("=== Creating Mercado Pago Preference ===");
            System.out.println("User ID: " + request.getUserId());
            System.out.println("User Type: " + request.getUserType());
            System.out.println("Email: " + request.getEmail());
            System.out.println("Price: " + request.getPrice());
            System.out.println("Title: " + request.getTitle());
            
            Preference preference = mercadoPagoService.createPreference(
                request.getTitle(),
                request.getDescription(),
                request.getPrice(),
                request.getQuantity(),
                request.getEmail(),
                request.getFirstName(),
                request.getLastName()
            );
            
            System.out.println("✅ Preference created successfully!");
            System.out.println("Preference ID: " + preference.getId());
            System.out.println("Init Point: " + preference.getInitPoint());
            System.out.println("Sandbox Init Point: " + preference.getSandboxInitPoint());
            
            // Store payment preference with user information
            // Si no hay userId, usar el email como identificador temporal
            String userIdToStore = request.getUserId() != null ? request.getUserId() : "email:" + request.getEmail();
            String userTypeToStore = request.getUserType() != null ? request.getUserType() : "UNKNOWN";
            
            subscriptionActivationService.storePaymentPreference(
                preference.getId(),
                userIdToStore,
                request.getEmail(),
                userTypeToStore,
                request.getPrice()
            );
            System.out.println("✅ Payment preference stored in database");
            System.out.println("Stored with userId: " + userIdToStore + ", userType: " + userTypeToStore);
            
            PreferenceResponse response = PreferenceResponse.builder()
                .preferenceId(preference.getId())
                .initPoint(preference.getInitPoint())
                .sandboxInitPoint(preference.getSandboxInitPoint())
                .message("Preferencia creada exitosamente")
                .build();
            
            return ResponseEntity.ok(response);
        } catch (com.mercadopago.exceptions.MPApiException e) {
            System.err.println("❌ Mercado Pago API Error:");
            System.err.println("Status Code: " + e.getStatusCode());
            System.err.println("Message: " + e.getMessage());
            
            // Intentar obtener más detalles del error
            try {
                if (e.getApiResponse() != null) {
                    System.err.println("API Response Content: " + e.getApiResponse().getContent());
                    System.err.println("API Response Status Code: " + e.getApiResponse().getStatusCode());
                }
            } catch (Exception ex) {
                System.err.println("No se pudo obtener detalles de la respuesta");
            }
            
            e.printStackTrace();
            
            PreferenceResponse errorResponse = PreferenceResponse.builder()
                .message("Error de API de Mercado Pago: " + e.getMessage() + " (Status: " + e.getStatusCode() + ")")
                .build();
            return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
        } catch (com.mercadopago.exceptions.MPException e) {
            System.err.println("❌ Mercado Pago SDK Error:");
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            
            PreferenceResponse errorResponse = PreferenceResponse.builder()
                .message("Error del SDK de Mercado Pago: " + e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            System.err.println("❌ Unexpected Error:");
            e.printStackTrace();
            
            PreferenceResponse errorResponse = PreferenceResponse.builder()
                .message("Error inesperado: " + e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Activar suscripción manualmente después del pago
     * Este endpoint se llama desde el frontend después de que el usuario regresa de Mercado Pago
     */
    @PostMapping("/activate-subscription")
    public ResponseEntity<String> activateSubscription(@RequestParam String paymentId,
                                                       @RequestParam String preferenceId) {
        try {
            System.out.println("=== Manual Subscription Activation ===");
            System.out.println("Payment ID: " + paymentId);
            System.out.println("Preference ID: " + preferenceId);
            
            // Obtener información del pago desde MercadoPago
            Payment payment = mercadoPagoService.getPayment(Long.parseLong(paymentId));
            
            System.out.println("Payment Status: " + payment.getStatus());
            
            // Obtener el email del pagador
            String payerEmail = payment.getPayer() != null ? payment.getPayer().getEmail() : null;
            
            // Procesar el pago y activar suscripción
            subscriptionActivationService.processPaymentNotification(
                payment.getId().toString(),
                payment.getStatus(),
                preferenceId,
                payerEmail
            );
            
            return ResponseEntity.ok("Suscripción activada exitosamente");
            
        } catch (Exception e) {
            System.err.println("Error activando suscripción: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    /**
     * Webhook para recibir notificaciones de Mercado Pago
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody(required = false) String payload,
                                               @RequestParam(required = false) String type,
                                               @RequestParam(required = false) String id,
                                               @RequestParam(required = false) String data_id) {
        try {
            System.out.println("=== Webhook recibido ===");
            System.out.println("Type: " + type);
            System.out.println("ID: " + id);
            System.out.println("Data ID: " + data_id);
            System.out.println("Payload: " + payload);
            
            // MercadoPago puede enviar el ID en diferentes parámetros
            String paymentId = data_id != null ? data_id : id;
            
            // Procesar notificación de pago
            if ("payment".equals(type) && paymentId != null) {
                try {
                    // Obtener información del pago desde MercadoPago
                    Payment payment = mercadoPagoService.getPayment(Long.parseLong(paymentId));
                    
                    System.out.println("Payment Status: " + payment.getStatus());
                    System.out.println("Payment ID: " + payment.getId());
                    
                    // Obtener el email del pagador para validación de seguridad
                    String payerEmail = payment.getPayer() != null ? payment.getPayer().getEmail() : null;
                    System.out.println("Payer Email: " + payerEmail);
                    
                    // Obtener el preference ID del pago
                    String preferenceId = payment.getOrder() != null && payment.getOrder().getId() != null 
                        ? String.valueOf(payment.getOrder().getId())
                        : null;
                    
                    if (preferenceId == null && payment.getExternalReference() != null) {
                        preferenceId = payment.getExternalReference();
                    }
                    
                    System.out.println("Preference ID: " + preferenceId);
                    
                    // Procesar el pago y activar suscripción si está aprobado
                    if (preferenceId != null) {
                        subscriptionActivationService.processPaymentNotification(
                            payment.getId().toString(),
                            payment.getStatus(),
                            preferenceId,
                            payerEmail  // Pasar el email del pagador para validación
                        );
                    } else {
                        System.err.println("No se pudo obtener el preference ID del pago");
                    }
                    
                } catch (Exception e) {
                    System.err.println("Error procesando pago: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            return ResponseEntity.ok("Webhook procesado");
            
        } catch (Exception e) {
            System.err.println("Error en webhook: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok("Webhook recibido con errores");
        }
    }
}
