package com.drawnet.artcollab.monetizationservice.infrastructure.persistence.jpa;

import com.drawnet.artcollab.monetizationservice.domain.model.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    
    Optional<UserSubscription> findByUserId(String userId);
    
    Optional<UserSubscription> findByUserEmail(String userEmail);
    
    Optional<UserSubscription> findByMercadoPagoSubscriptionId(String mercadoPagoSubscriptionId);
    
    boolean existsByUserIdAndIsActiveTrue(String userId);
}
