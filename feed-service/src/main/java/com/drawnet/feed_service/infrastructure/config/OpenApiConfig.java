package com.drawnet.feed_service.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * Configuración de OpenAPI/Swagger para documentación de la API
 */
@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:feed-service}")
    private String applicationName;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Bean
    @Primary
    public OpenAPI feedServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Feed Service API")
                        .description("API del servicio de feed social para ArtCollab - Gestión de posts, comentarios, reacciones y contenido multimedia")
                        .version(appVersion)
                        .contact(new Contact()
                                .name("DrawNet Team")
                                .email("support@drawnet.com")
                                .url("https://drawnet.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8083")
                                .description("Desarrollo Local"),
                        new Server()
                                .url("https://api-dev.artcollab.com/feed")
                                .description("Desarrollo"),
                        new Server()
                                .url("https://api-staging.artcollab.com/feed")
                                .description("Staging"),
                        new Server()
                                .url("https://api.artcollab.com/feed")
                                .description("Producción")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Token JWT para autenticación")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth"));
    }
}