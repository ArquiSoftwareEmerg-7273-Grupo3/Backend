package com.drawnet.artcollab.monetizationservice.application.service;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MercadoPagoService {
    
    /**
     * Crear un pago directo (con tarjeta tokenizada)
     */
    public Payment createPayment(BigDecimal amount, String description, 
                                String paymentMethodId, String email, 
                                String token, Integer installments) throws MPException, MPApiException {
        
        PaymentClient client = new PaymentClient();
        
        PaymentCreateRequest request = PaymentCreateRequest.builder()
            .transactionAmount(amount)
            .description(description)
            .paymentMethodId(paymentMethodId)
            .token(token)
            .installments(installments)
            .payer(PaymentPayerRequest.builder()
                .email(email)
                .build())
            .build();
        
        return client.create(request);
    }
    
    /**
     * Obtener información de un pago
     */
    public Payment getPayment(Long paymentId) throws MPException, MPApiException {
        PaymentClient client = new PaymentClient();
        return client.get(paymentId);
    }
    
    /**
     * Crear una preferencia de pago (Checkout Pro)
     * Esto genera un link de pago donde el usuario elige su método de pago
     */
    public Preference createPreference(String title, String description, 
                                      BigDecimal price, Integer quantity,
                                      String email, String firstName, 
                                      String lastName) throws MPException, MPApiException {
        
        PreferenceClient client = new PreferenceClient();
        
        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(PreferenceItemRequest.builder()
            .title(title)
            .description(description)
            .quantity(quantity)
            .currencyId("PEN")  // Cambia según tu país: MXN, COP, CLP, ARS, etc.
            .unitPrice(price)
            .build());
        
        // URLs de retorno - actualiza con tus URLs reales
        String baseUrl = "http://localhost:4200";
        
        PreferenceRequest request = PreferenceRequest.builder()
            .items(items)
            .payer(PreferencePayerRequest.builder()
                .email(email)
                .name(firstName)
                .surname(lastName)
                .build())
            .backUrls(PreferenceBackUrlsRequest.builder()
                .success(baseUrl + "/suscription/success")
                .failure(baseUrl + "/suscription/success")
                .pending(baseUrl + "/suscription/success")
                .build())
            // autoReturn removido - no funciona con localhost
            // .notificationUrl("https://your-ngrok-url.ngrok.io/api/mercadopago/webhook")  // Webhook - requiere URL pública
            .build();
        
        return client.create(request);
    }
    
}
