package com.drawnet.artcollab.monetizationservice.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanRequest {
    private String reason;  // Nombre del plan
    private BigDecimal autoRecurringAmount;  // Monto mensual
    private String frequency;  // 1 = mensual
    private Integer frequencyType;  // months
    private String backUrl;  // URL de retorno
}
