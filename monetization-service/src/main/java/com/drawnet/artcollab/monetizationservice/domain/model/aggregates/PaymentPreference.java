package com.drawnet.artcollab.monetizationservice.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_preferences", indexes = {
    @Index(name = "idx_preference_id", columnList = "preferenceId"),
    @Index(name = "idx_user_id", columnList = "userId"),
    @Index(name = "idx_status", columnList = "status")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPreference {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 255)
    private String preferenceId;  // MercadoPago preference ID
    
    @Column(nullable = false, length = 50)
    private String userId;
    
    @Column(nullable = false, length = 255)
    private String userEmail;
    
    @Column(nullable = false, length = 20)
    private String userType;  // "ILUSTRADOR" or "ESCRITOR"
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(nullable = false, length = 20)
    private String status;  // "pending", "completed", "failed"
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "pending";
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Business methods
    public void markAsCompleted() {
        this.status = "completed";
    }
    
    public void markAsFailed() {
        this.status = "failed";
    }
    
    public boolean isPending() {
        return "pending".equals(this.status);
    }
    
    public boolean isCompleted() {
        return "completed".equals(this.status);
    }
}
