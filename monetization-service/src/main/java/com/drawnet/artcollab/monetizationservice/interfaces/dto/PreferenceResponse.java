package com.drawnet.artcollab.monetizationservice.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreferenceResponse {
    private String preferenceId;
    private String initPoint;  // URL para redirigir al checkout
    private String sandboxInitPoint;  // URL para testing
    private String message;
}
