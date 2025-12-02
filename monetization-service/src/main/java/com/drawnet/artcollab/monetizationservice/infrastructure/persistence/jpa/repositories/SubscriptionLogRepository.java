package com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.repositories;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.SubscriptionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SubscriptionLogRepository extends JpaRepository<SubscriptionLog, Long> {
    
    /**
     * Find all subscription logs for a user, ordered by timestamp descending
     */
    List<SubscriptionLog> findByUserIdOrderByTimestampDesc(String userId);
    
    /**
     * Find all subscription logs by action type
     */
    List<SubscriptionLog> findByAction(String action);
    
    /**
     * Find all subscription logs by source
     */
    List<SubscriptionLog> findBySource(String source);
    
    /**
     * Find all subscription logs for a user by action
     */
    List<SubscriptionLog> findByUserIdAndAction(String userId, String action);
    
    /**
     * Find all subscription logs for a user by source
     */
    List<SubscriptionLog> findByUserIdAndSource(String userId, String source);
    
    /**
     * Find all subscription logs within a date range
     */
    @Query("SELECT s FROM SubscriptionLog s WHERE s.timestamp BETWEEN :startDate AND :endDate ORDER BY s.timestamp DESC")
    List<SubscriptionLog> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find all subscription logs performed by a specific admin
     */
    List<SubscriptionLog> findByPerformedBy(String performedBy);
    
    /**
     * Find subscription log by payment ID
     */
    List<SubscriptionLog> findByPaymentId(String paymentId);
}
