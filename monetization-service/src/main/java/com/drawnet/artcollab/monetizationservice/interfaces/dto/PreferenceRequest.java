package com.drawnet.artcollab.monetizationservice.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreferenceRequest {
    private String title;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String email;
    private String firstName;
    private String lastName;
    private String userId;      // ID del usuario
    private String userType;    // "ILUSTRADOR" o "ESCRITOR"
}
