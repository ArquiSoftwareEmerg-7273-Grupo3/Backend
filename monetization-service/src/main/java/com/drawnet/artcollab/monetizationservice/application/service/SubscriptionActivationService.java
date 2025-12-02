package com.drawnet.artcollab.monetizationservice.application.service;

import com.drawnet.artcollab.monetizationservice.domain.model.UserSubscription;
import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.PaymentPreference;
import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.PaymentRecord;
import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.SubscriptionLog;
import com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.UserSubscriptionRepository;
import com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.repositories.PaymentPreferenceRepository;
import com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.repositories.PaymentRecordRepository;
import com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.repositories.SubscriptionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionActivationService {
    
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final PaymentPreferenceRepository paymentPreferenceRepository;
    private final PaymentRecordRepository paymentRecordRepository;
    private final SubscriptionLogRepository subscriptionLogRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    
    private static final String AUTH_SERVICE_URL = "http://localhost:8080/api/v1/subscriptions";
    
    /**
     * Process payment notification and activate subscription if approved
     */
    @Transactional
    public void processPaymentNotification(String paymentId, String status, String preferenceId, String payerEmail) {
        try {
            System.out.println("Processing payment notification - PaymentId: " + paymentId + ", Status: " + status);
            
            // Only process approved payments
            if (!"approved".equalsIgnoreCase(status)) {
                System.out.println("Payment not approved, skipping activation. Status: " + status);
                return;
            }
            
            // Find the payment preference to get user information
            Optional<PaymentPreference> preferenceOpt = paymentPreferenceRepository.findByPreferenceId(preferenceId);
            if (preferenceOpt.isEmpty()) {
                System.err.println("Payment preference not found for preferenceId: " + preferenceId);
                return;
            }
            
            PaymentPreference preference = preferenceOpt.get();
            String userId = preference.getUserId();
            String userType = preference.getUserType();
            String registeredEmail = preference.getUserEmail();
            
            // Si el userId es temporal (email:xxx), intentar buscar el usuario real por email
            if (userId != null && userId.startsWith("email:")) {
                System.out.println("⚠️ UserId is temporary (email-based). Manual activation will be required.");
                System.out.println("User should log in and admin should manually activate subscription.");
                
                // Crear log para activación manual
                SubscriptionLog manualLog = SubscriptionLog.builder()
                        .userId(userId)
                        .action("pending_manual_activation")
                        .source("payment")
                        .paymentId(paymentId)
                        .reason("Payment successful but user was not logged in. Email: " + registeredEmail)
                        .timestamp(LocalDateTime.now())
                        .build();
                subscriptionLogRepository.save(manualLog);
                
                System.out.println("⚠️ Payment recorded but subscription NOT activated automatically.");
                System.out.println("Admin must manually activate for email: " + registeredEmail);
                return;
            }
            
            System.out.println("Found preference for user: " + userId + ", type: " + userType);
            System.out.println("Registered email: " + registeredEmail + ", Payer email: " + payerEmail);
            
            // SECURITY: Validate that the payer email matches the registered user email
            if (payerEmail != null && registeredEmail != null) {
                if (!payerEmail.equalsIgnoreCase(registeredEmail)) {
                    System.err.println("SECURITY ALERT: Email mismatch! Registered: " + registeredEmail + ", Payer: " + payerEmail);
                    System.err.println("Subscription activation BLOCKED for security reasons");
                    
                    // Create a log entry for security audit
                    SubscriptionLog securityLog = SubscriptionLog.builder()
                            .userId(userId)
                            .action("activation_blocked")
                            .source("payment")
                            .paymentId(paymentId)
                            .reason("Email mismatch - Security validation failed. Registered: " + registeredEmail + ", Payer: " + payerEmail)
                            .timestamp(LocalDateTime.now())
                            .build();
                    subscriptionLogRepository.save(securityLog);
                    
                    return; // Block activation
                }
            }
            
            System.out.println("Email validation passed. Proceeding with activation...");
            
            // Call Auth Service to activate subscription
            boolean activated = activateUserSubscription(userId, userType, paymentId);
            
            if (activated) {
                // Create UserSubscription record
                UserSubscription userSubscription = UserSubscription.builder()
                        .userId(userId)
                        .userEmail(preference.getUserEmail())
                        .userType(userType)
                        .mercadoPagoSubscriptionId(paymentId)
                        .mercadoPagoPlanId(preferenceId)
                        .status("active")
                        .isActive(true)
                        .startDate(LocalDateTime.now())
                        .build();
                userSubscriptionRepository.save(userSubscription);
                
                // Create subscription log
                SubscriptionLog log = SubscriptionLog.builder()
                        .userId(userId)
                        .action("activated")
                        .source("payment")
                        .paymentId(paymentId)
                        .reason("Payment approved and email validated")
                        .timestamp(LocalDateTime.now())
                        .build();
                subscriptionLogRepository.save(log);
                
                // Update preference status
                preference.markAsCompleted();
                paymentPreferenceRepository.save(preference);
                
                System.out.println("✅ Subscription activated successfully for user: " + userId);
            } else {
                System.err.println("❌ Failed to activate subscription for user: " + userId);
            }
            
        } catch (Exception e) {
            System.err.println("Error processing payment notification: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Call Auth Service to activate user subscription
     */
    private boolean activateUserSubscription(String userId, String userType, String paymentId) {
        try {
            String url = AUTH_SERVICE_URL + "/activate";
            
            Map<String, String> request = new HashMap<>();
            request.put("userId", userId);
            request.put("userType", userType);
            request.put("paymentId", paymentId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
            
            System.out.println("Calling Auth Service to activate subscription: " + url);
            System.out.println("Request: " + request);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            
            System.out.println("Auth Service response: " + response.getStatusCode());
            System.out.println("Response body: " + response.getBody());
            
            return response.getStatusCode().is2xxSuccessful();
            
        } catch (Exception e) {
            System.err.println("Error calling Auth Service: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Store payment preference
     */
    @Transactional
    public PaymentPreference storePaymentPreference(String preferenceId, String userId, String userEmail, 
                                                     String userType, java.math.BigDecimal amount) {
        PaymentPreference preference = PaymentPreference.builder()
                .preferenceId(preferenceId)
                .userId(userId)
                .userEmail(userEmail)
                .userType(userType)
                .amount(amount)
                .status("pending")
                .build();
        
        return paymentPreferenceRepository.save(preference);
    }
    
    /**
     * Store payment record
     */
    @Transactional
    public PaymentRecord storePaymentRecord(String paymentId, String preferenceId, String userId,
                                           String status, java.math.BigDecimal amount) {
        PaymentRecord record = PaymentRecord.builder()
                .paymentId(paymentId)
                .preferenceId(preferenceId)
                .userId(userId)
                .status(status)
                .amount(amount)
                .paidAt(LocalDateTime.now())
                .build();
        
        return paymentRecordRepository.save(record);
    }
}
