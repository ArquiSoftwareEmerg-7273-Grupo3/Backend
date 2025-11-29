package com.drawnet.artcollab.monetizationservice.application.service;

import com.drawnet.artcollab.monetizationservice.domain.model.UserSubscription;
import com.drawnet.artcollab.monetizationservice.infrastructure.client.AuthServiceClient;
import com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSubscriptionManagementService {
    
    private final UserSubscriptionRepository subscriptionRepository;
    private final AuthServiceClient authServiceClient;
    
    /**
     * Activar suscripción para un usuario
     */
    @Transactional
    public UserSubscription activateSubscription(
            String userId,
            String userEmail,
            String userType,
            String mercadoPagoSubscriptionId,
            String mercadoPagoPlanId
    ) {
        // Verificar si ya existe una suscripción para este usuario
        Optional<UserSubscription> existing = subscriptionRepository.findByUserId(userId);
        
        UserSubscription subscription;
        if (existing.isPresent()) {
            // Actualizar suscripción existente
            subscription = existing.get();
            subscription.setMercadoPagoSubscriptionId(mercadoPagoSubscriptionId);
            subscription.setMercadoPagoPlanId(mercadoPagoPlanId);
            subscription.setStatus("active");
            subscription.setIsActive(true);
            subscription.setStartDate(LocalDateTime.now());
            subscription.setEndDate(null);
            
            subscription = subscriptionRepository.save(subscription);
        } else {
            // Crear nueva suscripción
            subscription = UserSubscription.builder()
                    .userId(userId)
                    .userEmail(userEmail)
                    .userType(userType)
                    .mercadoPagoSubscriptionId(mercadoPagoSubscriptionId)
                    .mercadoPagoPlanId(mercadoPagoPlanId)
                    .status("active")
                    .isActive(true)
                    .startDate(LocalDateTime.now())
                    .build();
            
            subscription = subscriptionRepository.save(subscription);
        }
        
        // Llamar a auth-service para actualizar el campo subscripcion en Escritor/Ilustrador
        try {
            Long userIdLong = Long.parseLong(userId);
            log.info("Activando suscripción en auth-service para userId: {} tipo: {}", userIdLong, userType);
            authServiceClient.activateSubscription(userIdLong, userType);
            log.info("Suscripción activada exitosamente en auth-service");
        } catch (Exception e) {
            log.error("Error al activar suscripción en auth-service: {}", e.getMessage(), e);
            // No lanzamos excepción para no afectar el flujo principal
        }
        
        return subscription;
    }
    
    /**
     * Verificar si un usuario tiene suscripción activa
     */
    public boolean hasActiveSubscription(String userId) {
        return subscriptionRepository.existsByUserIdAndIsActiveTrue(userId);
    }
    
    /**
     * Obtener suscripción de un usuario
     */
    public Optional<UserSubscription> getUserSubscription(String userId) {
        return subscriptionRepository.findByUserId(userId);
    }
    
    /**
     * Cancelar suscripción
     */
    @Transactional
    public UserSubscription cancelSubscription(String userId) {
        UserSubscription subscription = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Suscripción no encontrada"));
        
        subscription.setStatus("cancelled");
        subscription.setIsActive(false);
        subscription.setEndDate(LocalDateTime.now());
        
        subscription = subscriptionRepository.save(subscription);
        
        // Llamar a auth-service para desactivar el campo subscripcion en Escritor/Ilustrador
        try {
            Long userIdLong = Long.parseLong(userId);
            log.info("Desactivando suscripción en auth-service para userId: {} tipo: {}", userIdLong, subscription.getUserType());
            authServiceClient.deactivateSubscription(userIdLong, subscription.getUserType());
            log.info("Suscripción desactivada exitosamente en auth-service");
        } catch (Exception e) {
            log.error("Error al desactivar suscripción en auth-service: {}", e.getMessage(), e);
        }
        
        return subscription;
    }
    
    /**
     * Pausar suscripción
     */
    @Transactional
    public UserSubscription pauseSubscription(String userId) {
        UserSubscription subscription = subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Suscripción no encontrada"));
        
        subscription.setStatus("paused");
        subscription.setIsActive(false);
        
        subscription = subscriptionRepository.save(subscription);
        
        // Llamar a auth-service para desactivar el campo subscripcion en Escritor/Ilustrador
        try {
            Long userIdLong = Long.parseLong(userId);
            log.info("Pausando suscripción en auth-service para userId: {} tipo: {}", userIdLong, subscription.getUserType());
            authServiceClient.deactivateSubscription(userIdLong, subscription.getUserType());
            log.info("Suscripción pausada exitosamente en auth-service");
        } catch (Exception e) {
            log.error("Error al pausar suscripción en auth-service: {}", e.getMessage(), e);
        }
        
        return subscription;
    }
}
