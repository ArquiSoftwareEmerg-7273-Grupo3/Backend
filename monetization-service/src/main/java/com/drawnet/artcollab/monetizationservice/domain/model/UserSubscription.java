package com.drawnet.artcollab.monetizationservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_subscriptions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String userId;  // ID del usuario (ilustrador o escritor)
    
    @Column(nullable = false)
    private String userEmail;
    
    @Column(nullable = false)
    private String userType;  // "ILUSTRADOR" o "ESCRITOR"
    
    @Column(nullable = false)
    private String mercadoPagoSubscriptionId;  // ID de la suscripción en Mercado Pago
    
    @Column(nullable = false)
    private String mercadoPagoPlanId;  // ID del plan en Mercado Pago
    
    @Column(nullable = false)
    private String status;  // "active", "paused", "cancelled"
    
    @Column(nullable = false)
    private Boolean isActive;  // true si la suscripción está activa
    
    @Column(nullable = false)
    private LocalDateTime startDate;
    
    @Column
    private LocalDateTime endDate;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
