package com.yeojeong.application.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "여정",
                description = "여정 API",
                version = "v1"))
@Configuration
public class SwaggerConfig {
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
        String[] paths = {"/token", "/token/**"};

        return GroupedOpenApi.builder()
                .group("토큰 API")
                .pathsToMatch(paths)
                .build();
    }
}
