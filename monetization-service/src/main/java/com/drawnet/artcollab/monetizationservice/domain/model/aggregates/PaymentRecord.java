package com.drawnet.artcollab.monetizationservice.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_records", indexes = {
    @Index(name = "idx_payment_id", columnList = "paymentId"),
    @Index(name = "idx_preference_id", columnList = "preferenceId"),
    @Index(name = "idx_user_id", columnList = "userId"),
    @Index(name = "idx_status", columnList = "status")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 255)
    private String paymentId;  // MercadoPago payment ID
    
    @Column(nullable = false, length = 255)
    private String preferenceId;
    
    @Column(nullable = false, length = 50)
    private String userId;
    
    @Column(nullable = false, length = 20)
    private String status;  // "approved", "rejected", "pending", "in_process"
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(length = 50)
    private String paymentMethod;
    
    @Column(length = 50)
    private String paymentType;
    
    @Column
    private LocalDateTime paidAt;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Business methods
    public boolean isApproved() {
        return "approved".equals(this.status);
    }
    
    public boolean isRejected() {
        return "rejected".equals(this.status);
    }
    
    public boolean isPending() {
        return "pending".equals(this.status) || "in_process".equals(this.status);
    }
}
