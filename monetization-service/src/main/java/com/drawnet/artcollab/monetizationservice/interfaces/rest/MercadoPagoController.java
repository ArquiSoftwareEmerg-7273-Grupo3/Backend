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
            Preference preference = mercadoPagoService.createPreference(
                request.getTitle(),
                request.getDescription(),
                request.getPrice(),
                request.getQuantity(),
                request.getEmail(),
                request.getFirstName(),
                request.getLastName()
            );
            
            PreferenceResponse response = PreferenceResponse.builder()
                .preferenceId(preference.getId())
                .initPoint(preference.getInitPoint())
                .sandboxInitPoint(preference.getSandboxInitPoint())
                .message("Preferencia creada exitosamente")
                .build();
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            PreferenceResponse errorResponse = PreferenceResponse.builder()
                .message("Error al crear preferencia: " + e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Webhook para recibir notificaciones de Mercado Pago
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                               @RequestParam(required = false) String type,
                                               @RequestParam(required = false) Long id) {
        // Tipos de notificaciones:
        // - payment: Notificación de pago
        // - merchant_order: Notificación de orden
        
        System.out.println("Webhook recibido - Type: " + type + ", ID: " + id);
        System.out.println("Payload: " + payload);
        
        // Aquí procesas la notificación según el tipo
        if ("payment".equals(type)) {
            // Actualizar estado del pago en tu base de datos
        }
        
        return ResponseEntity.ok("Webhook procesado");
    }
}
