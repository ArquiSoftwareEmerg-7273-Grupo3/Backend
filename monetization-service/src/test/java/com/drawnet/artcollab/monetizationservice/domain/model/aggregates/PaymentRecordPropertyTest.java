package com.drawnet.artcollab.monetizationservice.domain.model.aggregates;

import net.jqwik.api.*;
import net.jqwik.api.constraints.BigRange;
import net.jqwik.api.constraints.NotBlank;
import net.jqwik.api.constraints.StringLength;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Property-based tests for PaymentRecord entity
 * Feature: mercadopago-subscription-flow, Property 13: Payment completion creates payment record
 * Validates: Requirements 5.2
 */
class PaymentRecordPropertyTest {
    
    @Property(tries = 100)
    @Label("Payment record creation includes all required fields")
    void paymentRecordCreationIncludesAllRequiredFields(
            @ForAll @NotBlank @StringLength(min = 10, max = 50) String paymentId,
            @ForAll @NotBlank @StringLength(min = 10, max = 50) String preferenceId,
            @ForAll @NotBlank @StringLength(min = 1, max = 20) String userId,
            @ForAll("paymentStatus") String status,
            @ForAll @BigRange(min = "2.00", max = "1000.00") BigDecimal amount
    ) {
        // Given: Valid payment data
        PaymentRecord record = PaymentRecord.builder()
                .paymentId(paymentId)
                .preferenceId(preferenceId)
                .userId(userId)
                .status(status)
                .amount(amount)
                .paymentMethod("credit_card")
                .paymentType("credit_card")
                .paidAt(LocalDateTime.now())
                .build();
        
        // When: Creating a payment record
        record.onCreate();
        
        // Then: The record should contain all required fields
        assertThat(record.getPaymentId()).isEqualTo(paymentId);
        assertThat(record.getPreferenceId()).isEqualTo(preferenceId);
        assertThat(record.getUserId()).isEqualTo(userId);
        assertThat(record.getStatus()).isEqualTo(status);
        assertThat(record.getAmount()).isEqualTo(amount);
        assertThat(record.getCreatedAt()).isNotNull();
    }
    
    @Property(tries = 100)
    @Label("Payment record status checks work correctly")
    void paymentRecordStatusChecksWorkCorrectly(
            @ForAll @NotBlank @StringLength(min = 10, max = 50) String paymentId,
            @ForAll @NotBlank @StringLength(min = 10, max = 50) String preferenceId,
            @ForAll @NotBlank @StringLength(min = 1, max = 20) String userId,
            @ForAll("paymentStatus") String status,
            @ForAll @BigRange(min = "2.00", max = "1000.00") BigDecimal amount
    ) {
        // Given: A payment record with a specific status
        PaymentRecord record = PaymentRecord.builder()
                .paymentId(paymentId)
                .preferenceId(preferenceId)
                .userId(userId)
                .status(status)
                .amount(amount)
                .build();
        record.onCreate();
        
        // Then: Status check methods should return correct values
        if ("approved".equals(status)) {
            assertThat(record.isApproved()).isTrue();
            assertThat(record.isRejected()).isFalse();
            assertThat(record.isPending()).isFalse();
        } else if ("rejected".equals(status)) {
            assertThat(record.isApproved()).isFalse();
            assertThat(record.isRejected()).isTrue();
            assertThat(record.isPending()).isFalse();
        } else if ("pending".equals(status) || "in_process".equals(status)) {
            assertThat(record.isApproved()).isFalse();
            assertThat(record.isRejected()).isFalse();
            assertThat(record.isPending()).isTrue();
        }
    }
    
    @Property(tries = 100)
    @Label("Payment record with approved status is correctly identified")
    void paymentRecordWithApprovedStatusIsCorrectlyIdentified(
            @ForAll @NotBlank @StringLength(min = 10, max = 50) String paymentId,
            @ForAll @NotBlank @StringLength(min = 10, max = 50) String preferenceId,
            @ForAll @NotBlank @StringLength(min = 1, max = 20) String userId,
            @ForAll @BigRange(min = "2.00", max = "1000.00") BigDecimal amount
    ) {
        // Given: A payment record with approved status
        PaymentRecord record = PaymentRecord.builder()
                .paymentId(paymentId)
                .preferenceId(preferenceId)
                .userId(userId)
                .status("approved")
                .amount(amount)
                .build();
        record.onCreate();
        
        // Then: isApproved should return true
        assertThat(record.isApproved()).isTrue();
        assertThat(record.getStatus()).isEqualTo("approved");
    }
    
    @Property(tries = 100)
    @Label("Payment record timestamp is set on creation")
    void paymentRecordTimestampIsSetOnCreation(
            @ForAll @NotBlank @StringLength(min = 10, max = 50) String paymentId,
            @ForAll @NotBlank @StringLength(min = 10, max = 50) String preferenceId,
            @ForAll @NotBlank @StringLength(min = 1, max = 20) String userId,
            @ForAll("paymentStatus") String status,
            @ForAll @BigRange(min = "2.00", max = "1000.00") BigDecimal amount
    ) {
        // Given: A payment record
        PaymentRecord record = PaymentRecord.builder()
                .paymentId(paymentId)
                .preferenceId(preferenceId)
                .userId(userId)
                .status(status)
                .amount(amount)
                .build();
        
        LocalDateTime beforeCreation = LocalDateTime.now();
        
        // When: onCreate is called
        record.onCreate();
        
        LocalDateTime afterCreation = LocalDateTime.now();
        
        // Then: createdAt should be set between before and after
        assertThat(record.getCreatedAt()).isNotNull();
        assertThat(record.getCreatedAt()).isAfterOrEqualTo(beforeCreation);
        assertThat(record.getCreatedAt()).isBeforeOrEqualTo(afterCreation);
    }
    
    @Provide
    Arbitrary<String> paymentStatus() {
        return Arbitraries.of("approved", "rejected", "pending", "in_process");
    }
}
