package com.faroc.gymanager.common.api.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        final String API_TITLE = "Gymanager";
        final String SECURITY_SCHEME = "Gymanager Security Scheme";

        return new OpenAPI()
                .info(new Info().title(API_TITLE))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME, new SecurityScheme()
                        .name(SECURITY_SCHEME).type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));

    }
}
