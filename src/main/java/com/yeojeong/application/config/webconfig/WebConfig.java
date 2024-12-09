package com.yeojeong.application.config.webconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${filePath}")
    private String PATH;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String viewPath = "/files/**";
        registry.addResourceHandler(viewPath)
                .addResourceLocations("file:///" + PATH)
                .resourceChain(true)
                .addResolver(new ImageResourceResolver(PATH));
    }
}