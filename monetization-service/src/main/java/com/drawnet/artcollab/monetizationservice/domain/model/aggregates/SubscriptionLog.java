package com.drawnet.artcollab.monetizationservice.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscription_logs", indexes = {
    @Index(name = "idx_user_id", columnList = "userId"),
    @Index(name = "idx_action", columnList = "action"),
    @Index(name = "idx_source", columnList = "source"),
    @Index(name = "idx_timestamp", columnList = "timestamp"),
    @Index(name = "idx_payment_id", columnList = "paymentId")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String userId;
    
    @Column(nullable = false, length = 50)
    private String action;  // "activated", "deactivated", "activation_blocked"
    
    @Column(nullable = false, length = 20)
    private String source;  // "payment", "manual", "admin"
    
    @Column(length = 50)
    private String performedBy;  // admin user ID if manual
    
    @Column(length = 500)
    private String reason;
    
    @Column(length = 255)
    private String paymentId;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;
    
    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
    
    // Business methods
    public boolean isPaymentSource() {
        return "payment".equals(this.source);
    }
    
    public boolean isManualSource() {
        return "manual".equals(this.source) || "admin".equals(this.source);
    }
    
    public boolean isActivation() {
        return "activated".equals(this.action);
    }
    
    public boolean isDeactivation() {
        return "deactivated".equals(this.action);
    }
}
