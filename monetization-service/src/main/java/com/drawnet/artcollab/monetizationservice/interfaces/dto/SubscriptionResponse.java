package com.drawnet.artcollab.monetizationservice.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponse {
    private String subscriptionId;
    private String customerId;
    private String status;
    private String clientSecret;
    private String message;
}
