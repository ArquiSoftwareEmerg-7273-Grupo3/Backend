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
public class PlanResponse {
    private String planId;
    private String reason;
    private BigDecimal autoRecurringAmount;
    private String frequency;
    private String status;
    private String initPoint;  // URL para suscribirse
    private String message;
}
