package com.drawnet.artcollab.portafolioservice.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI ArtCollabOpenApi() {
        // Definir el esquema de seguridad JWT
        final String securitySchemeName = "bearerAuth";
        
        var openApi = new OpenAPI();
        openApi
                .info(new Info()
                        .title("Portfolio Service API")
                        .description("ArtCollab Portfolio REST API documentation with JWT Authentication.")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .externalDocs(new ExternalDocumentation()
                        .description("ArtCollab Wiki Documentation")
                        .url(""))
                // Agregar componente de seguridad
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Ingrese el token JWT obtenido del endpoint /api/v1/auth/login")))
                // Agregar requisito de seguridad global
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));

        return openApi;
    }
}

