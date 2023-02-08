package com.shoplive.converter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final String resourcePath;
    private final String connectPath;

    public WebConfig(
            @Value("${custom.path.resource}") String resourcePath,
            @Value("${custom.path.connect}") String connectPath
    ){
        this.resourcePath = resourcePath;
        this.connectPath = connectPath;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler(connectPath)
                .addResourceLocations(resourcePath);
    }
}
