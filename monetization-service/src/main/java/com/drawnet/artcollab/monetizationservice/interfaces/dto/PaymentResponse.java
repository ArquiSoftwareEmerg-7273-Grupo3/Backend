package com.drawnet.artcollab.monetizationservice.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long paymentId;
    private String status;
    private String statusDetail;
    private BigDecimal amount;
    private String currency;
    private String message;
}
