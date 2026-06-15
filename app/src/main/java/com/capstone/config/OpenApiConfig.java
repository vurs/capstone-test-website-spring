package com.capstone.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI capstoneOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Capstone Test Website API")
                        .description(
                                "REST endpoints for the capstone vulnerability scanner test application."
                        )
                        .version("1.0"));
    }
}
