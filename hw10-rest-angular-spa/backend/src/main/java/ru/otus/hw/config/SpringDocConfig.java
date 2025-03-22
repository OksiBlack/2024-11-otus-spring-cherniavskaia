package ru.otus.hw.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI bookStoreOpenApi() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes("basicScheme",
                new SecurityScheme().type(SecurityScheme.Type.OAUTH2)
                    .scheme("OAUTH2")))
            .info(new Info().title("Book Store API").version("1.0.0"));
    }
}
