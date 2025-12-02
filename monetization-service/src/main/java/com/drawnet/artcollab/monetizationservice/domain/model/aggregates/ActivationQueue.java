package com.drawnet.artcollab.monetizationservice.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "activation_queue", indexes = {
    @Index(name = "idx_user_id", columnList = "userId"),
    @Index(name = "idx_payment_id", columnList = "paymentId"),
    @Index(name = "idx_next_retry", columnList = "nextRetryAt"),
    @Index(name = "idx_retry_count", columnList = "retryCount")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivationQueue {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String userId;
    
    @Column(nullable = false, length = 20)
    private String userType;  // "ILUSTRADOR" or "ESCRITOR"
    
    @Column(nullable = false, length = 255)
    private String paymentId;
    
    @Column(nullable = false)
    private Integer retryCount;
    
    @Column(length = 1000)
    @Lob
    private String lastError;
    
    @Column(nullable = false)
    private LocalDateTime nextRetryAt;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (retryCount == null) {
            retryCount = 0;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Business methods
    public void incrementRetryCount() {
        this.retryCount++;
    }
    
    public void updateLastError(String error) {
        this.lastError = error;
    }
    
    public void calculateNextRetry() {
        // Exponential backoff: 1min, 2min, 4min
        long delayMinutes = (long) Math.pow(2, this.retryCount);
        this.nextRetryAt = LocalDateTime.now().plusMinutes(delayMinutes);
    }
    
    public boolean hasExceededMaxRetries() {
        return this.retryCount >= 3;
    }
    
    public boolean isReadyForRetry() {
        return LocalDateTime.now().isAfter(this.nextRetryAt);
    }
}
