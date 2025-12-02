package com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.repositories;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.PaymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    
    /**
     * Find payment record by MercadoPago payment ID
     */
    Optional<PaymentRecord> findByPaymentId(String paymentId);
    
    /**
     * Find all payment records for a user
     */
    List<PaymentRecord> findByUserId(String userId);
    
    /**
     * Find all payment records by preference ID
     */
    List<PaymentRecord> findByPreferenceId(String preferenceId);
    
    /**
     * Find all payment records by status
     */
    List<PaymentRecord> findByStatus(String status);
    
    /**
     * Find all approved payments for a user
     */
    List<PaymentRecord> findByUserIdAndStatus(String userId, String status);
    
    /**
     * Check if a payment ID already exists
     */
    boolean existsByPaymentId(String paymentId);
}
