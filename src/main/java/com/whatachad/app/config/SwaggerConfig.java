package com.whatachad.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "What A Chad API",
                description = "What A Chad에서 개발 중인 서비스 API 문서",
                version = "v1"))
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi demoOpenApi() { // application.yml 을 통해서도 설정 가능
        String[] paths = {"/api/demo/**"};

        return GroupedOpenApi.builder()
                .group("Demo API v1")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi whatachadOpenApi() {
        String[] paths = {"/v1/**"};

        return GroupedOpenApi.builder()
                .group("What A Chad API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public OpenAPI securityOpenApi() {
        final String BEARER_AUTH = "bearerAuth";
        final String DEBUG_MODE = "debug";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
//                        .addList(BEARER_AUTH)
                        .addList(DEBUG_MODE))
                .components(new Components()
//                        .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
//                                .name(BEARER_AUTH)
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("bearer")
//                                .bearerFormat("JWT"))
                        .addSecuritySchemes(DEBUG_MODE, new SecurityScheme() // TODO : 디버그 모드를 배포 단계에서 삭제해야 함
                                .name("Authorization")
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .description("API-KEY")));
    }
}
