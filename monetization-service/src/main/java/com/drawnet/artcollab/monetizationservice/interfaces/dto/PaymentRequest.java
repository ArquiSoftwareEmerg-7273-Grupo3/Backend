package com.drawnet.artcollab.monetizationservice.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private BigDecimal amount;
    private String description;
    private String paymentMethodId;  // visa, master, etc.
    private String email;
    private String token;  // Token de la tarjeta generado en frontend
    private Integer installments;  // Número de cuotas
}
