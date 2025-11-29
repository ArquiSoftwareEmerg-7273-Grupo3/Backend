package com.drawnet.artcollab.monetizationservice.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequest {
    private String preapprovalPlanId;  // ID del plan creado previamente
    private String cardTokenId;  // Token de la tarjeta del frontend
    private String email;
    private String backUrl;  // URL de retorno después de suscribirse
}
