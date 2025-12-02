package com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa.repositories;

import com.drawnet.artcollab.monetizationservice.domain.model.aggregates.PaymentPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentPreferenceRepository extends JpaRepository<PaymentPreference, Long> {
    
    /**
     * Find payment preference by MercadoPago preference ID
     */
    Optional<PaymentPreference> findByPreferenceId(String preferenceId);
    
    /**
     * Find all payment preferences for a user
     */
    List<PaymentPreference> findByUserId(String userId);
    
    /**
     * Find all payment preferences by status
     */
    List<PaymentPreference> findByStatus(String status);
    
    /**
     * Find all pending payment preferences for a user
     */
    List<PaymentPreference> findByUserIdAndStatus(String userId, String status);
    
    /**
     * Check if a preference ID already exists
     */
    boolean existsByPreferenceId(String preferenceId);
}
