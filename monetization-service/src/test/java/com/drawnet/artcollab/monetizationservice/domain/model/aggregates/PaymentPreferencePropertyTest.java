package com.drawnet.artcollab.monetizationservice.domain.model.aggregates;

import net.jqwik.api.*;
import net.jqwik.api.constraints.BigRange;
import net.jqwik.api.constraints.NotBlank;
import net.jqwik.api.constraints.StringLength;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Property-based tests for PaymentPreference entity
 * Feature: mercadopago-subscription-flow, Property 1: Payment preference creation includes all required fields
 * Validates: Requirements 5.1
 */
class PaymentPreferencePropertyTest {
    
    @Property(tries = 100)
    @Label("Payment preference creation includes all required fields")
    void paymentPreferenceCreationIncludesAllRequiredFields(
            @ForAll @NotBlank @StringLength(min = 10, max = 50) String preferenceId,
            @ForAll @NotBlank @StringLength(min = 1, max = 20) String userId,
            @ForAll @NotBlank @StringLength(min = 5, max = 100) String userEmail,
            @ForAll("userType") String userType,
            @ForAll @BigRange(min = "2.00", max = "1000.00") BigDecimal amount
    ) {
        // Given: Valid user data and subscription amount
        PaymentPreference preference = PaymentPreference.builder()
                .preferenceId(preferenceId)
                .userId(userId)
                .userEmail(userEmail)
                .userType(userType)
                .amount(amount)
                .build();
        
        // When: Creating a payment preference
        preference.onCreate();
        
        // Then: The preference object should contain all required fields
        assertThat(preference.getPreferenceId()).isEqualTo(preferenceId);
        assertThat(preference.getUserId()).isEqualTo(userId);
        assertThat(preference.getUserEmail()).isEqualTo(userEmail);
        assertThat(preference.getUserType()).isEqualTo(userType);
        assertThat(preference.getAmount()).isEqualTo(amount);
        assertThat(preference.getStatus()).isEqualTo("pending");
        assertThat(preference.getCreatedAt()).isNotNull();
        assertThat(preference.getUpdatedAt()).isNotNull();
    }
    
    @Property(tries = 100)
    @Label("Payment preference status transitions work correctly")
    void paymentPreferenceStatusTransitionsWorkCorrectly(
            @ForAll @NotBlank @StringLength(min = 10, max = 50) String preferenceId,
            @ForAll @NotBlank @StringLength(min = 1, max = 20) String userId,
            @ForAll @NotBlank @StringLength(min = 5, max = 100) String userEmail,
            @ForAll("userType") String userType,
            @ForAll @BigRange(min = "2.00", max = "1000.00") BigDecimal amount
    ) {
        // Given: A payment preference
        PaymentPreference preference = PaymentPreference.builder()
                .preferenceId(preferenceId)
                .userId(userId)
                .userEmail(userEmail)
                .userType(userType)
                .amount(amount)
                .build();
        preference.onCreate();
        
        // When: Marking as completed
        preference.markAsCompleted();
        
        // Then: Status should be completed
        assertThat(preference.getStatus()).isEqualTo("completed");
        assertThat(preference.isCompleted()).isTrue();
        assertThat(preference.isPending()).isFalse();
        
        // When: Marking as failed
        preference.markAsFailed();
        
        // Then: Status should be failed
        assertThat(preference.getStatus()).isEqualTo("failed");
        assertThat(preference.isCompleted()).isFalse();
        assertThat(preference.isPending()).isFalse();
    }
    
    @Property(tries = 100)
    @Label("Payment preference default status is pending")
    void paymentPreferenceDefaultStatusIsPending(
            @ForAll @NotBlank @StringLength(min = 10, max = 50) String preferenceId,
            @ForAll @NotBlank @StringLength(min = 1, max = 20) String userId,
            @ForAll @NotBlank @StringLength(min = 5, max = 100) String userEmail,
            @ForAll("userType") String userType,
            @ForAll @BigRange(min = "2.00", max = "1000.00") BigDecimal amount
    ) {
        // Given: A payment preference without explicit status
        PaymentPreference preference = PaymentPreference.builder()
                .preferenceId(preferenceId)
                .userId(userId)
                .userEmail(userEmail)
                .userType(userType)
                .amount(amount)
                .build();
        
        // When: onCreate is called
        preference.onCreate();
        
        // Then: Status should default to pending
        assertThat(preference.getStatus()).isEqualTo("pending");
        assertThat(preference.isPending()).isTrue();
    }
    
    @Provide
    Arbitrary<String> userType() {
        return Arbitraries.of("ILUSTRADOR", "ESCRITOR");
    }
}
