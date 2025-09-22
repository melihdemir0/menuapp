package com.application.menuapp.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiDocs() {
        return new OpenAPI()
            .info(new Info()
                .title("MobyDick Menu API")
                .description("QR menü sistemi için REST API")
                .version("v1"));
    }
}
