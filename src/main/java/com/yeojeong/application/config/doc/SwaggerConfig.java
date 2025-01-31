package com.yeojeong.application.config.doc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@OpenAPIDefinition(
        info = @Info(title = "여정",
                description = "여정 API",
                version = "v1"))
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                );
    }

    @Bean
    public GroupedOpenApi group1() {
        String[] paths = {"/members", "/members/**"};

        return GroupedOpenApi.builder()
                .group("유저 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi group2() {
        String[] paths = {"/boards", "/boards/**"};

        return GroupedOpenApi.builder()
                .group("게시글 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi group3() {
        String[] paths = {"/planners", "/planners/**"};

        return GroupedOpenApi.builder()
                .group("플래너 API")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi group4() {
        String[] paths = {"/tokens", "/tokens/**"};

        return GroupedOpenApi.builder()
                .group("토큰 API")
                .pathsToMatch(paths)
                .build();
    }
}