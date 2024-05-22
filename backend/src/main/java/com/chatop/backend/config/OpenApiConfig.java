package com.chatop.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    /**
     * Cette méthode configure l'instance OpenAPI pour la documentation de l'API.
     *
     * @return Une instance de OpenAPI configurée avec les schémas de sécurité.
     */
    @Bean
    public OpenAPI customnOpenApiConfig() {
        final String securitySchemeName = "bearerAuth"; // Nom du schéma de sécurité utilisé pour l'authentification Bearer.

        // Crée et configure une instance de OpenAPI.
        return new OpenAPI()
                // Ajoute un élément de sécurité à la configuration OpenAPI.
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName) // Spécifie le schéma de sécurité à utiliser.
                )
                // Configure les composants OpenAPI, y compris les schémas de sécurité.
                .components(
                        new Components()
                                // Ajoute un schéma de sécurité pour l'authentification Bearer.
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}

