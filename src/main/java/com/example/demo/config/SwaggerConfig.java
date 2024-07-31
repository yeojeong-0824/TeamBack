package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("여행 맛집 사이트 API")
                .version("v0.1")
                .description("일본과 한국 여행객을 대상으로 데모 버전 개발");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
