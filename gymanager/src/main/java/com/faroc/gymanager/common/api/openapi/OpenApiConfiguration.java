package com.faroc.gymanager.common.api.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    final String[] VERSION_PACKAGES_TO_SCAN = {
            "com.faroc.gymanager.gymmanagement.api",
            "com.faroc.gymanager.sessionmanagement.api",
            "com.faroc.gymanager.usermanagement.api"
    };

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

    @Bean
    public GroupedOpenApi v1GroupOpenApi() {
        final String GROUP_NAME = "V1";
        String[] pathMatch = {"/v1/**"};

        return GroupedOpenApi.builder().group(GROUP_NAME)
                .pathsToMatch(pathMatch)
                .packagesToScan(VERSION_PACKAGES_TO_SCAN)
                .build();
    }

    @Bean
    public GroupedOpenApi v2GroupOpenApi() {
        final String GROUP_NAME = "V2";
        String[] pathMatch = {"/v2/**"};

        return GroupedOpenApi.builder().group(GROUP_NAME)
                .pathsToMatch(pathMatch)
                .packagesToScan(VERSION_PACKAGES_TO_SCAN)
                .build();
    }
}
