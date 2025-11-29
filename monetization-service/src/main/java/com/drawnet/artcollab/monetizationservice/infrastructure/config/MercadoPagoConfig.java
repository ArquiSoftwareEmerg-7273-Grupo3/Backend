package com.drawnet.artcollab.monetizationservice.infrastructure.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MercadoPagoConfig {
    
    @Value("${mercadopago.access.token}")
    private String accessToken;
    
    @PostConstruct
    public void init() {
        System.out.println("Inicializando Mercado Pago...");
        System.out.println("Access Token configurado: " + (accessToken != null && !accessToken.isEmpty() ? "Sí" : "No"));
        if (accessToken != null && accessToken.length() > 10) {
            System.out.println("Token (primeros 20 caracteres): " + accessToken.substring(0, 20) + "...");
        }
        com.mercadopago.MercadoPagoConfig.setAccessToken(accessToken);
    }
    
    @Bean
    public WebClient mercadoPagoWebClient() {
        System.out.println("Creando WebClient para Mercado Pago");
        
        // Configurar timeout más largo
        io.netty.channel.ChannelOption channelOption = io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
        reactor.netty.http.client.HttpClient httpClient = reactor.netty.http.client.HttpClient.create()
            .option(channelOption, 30000) // 30 segundos de timeout de conexión
            .responseTimeout(java.time.Duration.ofSeconds(60)); // 60 segundos de timeout de respuesta
        
        return WebClient.builder()
            .baseUrl("https://api.mercadopago.com")
            .defaultHeader("Authorization", "Bearer " + accessToken)
            .defaultHeader("Content-Type", "application/json")
            .clientConnector(new org.springframework.http.client.reactive.ReactorClientHttpConnector(httpClient))
            .build();
    }
}
