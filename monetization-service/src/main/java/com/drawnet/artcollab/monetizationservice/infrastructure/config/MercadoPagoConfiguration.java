package com.drawnet.artcollab.monetizationservice.infrastructure.config;

import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MercadoPagoConfiguration {
    
    @Value("${mercadopago.access-token}")
    private String accessToken;
    
    @PostConstruct
    public void init() {
        // Configurar el SDK de Mercado Pago con el access token
        MercadoPagoConfig.setAccessToken(accessToken);
        System.out.println("✅ Mercado Pago SDK configurado correctamente");
        System.out.println("Access Token: " + accessToken.substring(0, Math.min(20, accessToken.length())) + "...");
    }
    
    /**
     * WebClient configurado para llamadas a la API de Mercado Pago
     */
    @Bean
    public WebClient mercadoPagoWebClient() {
        return WebClient.builder()
            .baseUrl("https://api.mercadopago.com")
            .defaultHeader("Authorization", "Bearer " + accessToken)
            .defaultHeader("Content-Type", "application/json")
            .build();
    }
}
