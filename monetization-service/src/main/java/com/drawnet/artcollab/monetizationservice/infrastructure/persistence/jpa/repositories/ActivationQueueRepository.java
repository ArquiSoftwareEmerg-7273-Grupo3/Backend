package com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.repositories;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.ActivationQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActivationQueueRepository extends JpaRepository<ActivationQueue, Long> {
    
    /**
     * Find activation queue entry by user ID
     */
    Optional<ActivationQueue> findByUserId(String userId);
    
    /**
     * Find activation queue entry by payment ID
     */
    Optional<ActivationQueue> findByPaymentId(String paymentId);
    
    /**
     * Find all entries ready for retry (nextRetryAt is in the past)
     */
    @Query("SELECT a FROM ActivationQueue a WHERE a.nextRetryAt <= :now AND a.retryCount < 3")
    List<ActivationQueue> findReadyForRetry(LocalDateTime now);
    
    /**
     * Find all entries that have exceeded max retries
     */
    @Query("SELECT a FROM ActivationQueue a WHERE a.retryCount >= 3")
    List<ActivationQueue> findExceededMaxRetries();
    
    /**
     * Delete activation queue entry by user ID
     */
    void deleteByUserId(String userId);
    
    /**
     * Delete activation queue entry by payment ID
     */
    void deleteByPaymentId(String paymentId);
}
