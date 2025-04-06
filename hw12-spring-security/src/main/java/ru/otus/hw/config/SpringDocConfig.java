package ru.otus.hw.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Value("${spring.security.oauth2.client.provider.keycloak.token-uri:}")
    private String tokenUrl;


    @Bean
    public OpenAPI bookStoreOpenApi() {
        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement().addList("bearerToken"))
            .addSecurityItem(new SecurityRequirement().addList("clientCredentials"))
            .addSecurityItem(new SecurityRequirement().addList("resourceOwnerPassword"))
            .components(new Components()
                .addSecuritySchemes("bearerToken",
                    createBearerTokenSchema()
                )
                .addSecuritySchemes("clientCredentials",
                    createClientCredentialsSchema()
                )
                .addSecuritySchemes("resourceOwnerPassword",
                    createResourceOwnerSchema()
                )
            )
            .info(new Info().title("Book Store API").version("1.0.0"));
    }

    private SecurityScheme createBearerTokenSchema() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");
    }

    private SecurityScheme createResourceOwnerSchema() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.OAUTH2)
            .flows(new OAuthFlows()
                .password(new OAuthFlow()
                    .tokenUrl(tokenUrl)
                    .scopes(new Scopes())
                )
            );
    }

    private SecurityScheme createClientCredentialsSchema() {
        return new SecurityScheme()
            .type(SecurityScheme.Type.OAUTH2)
            .flows(new OAuthFlows()
                .clientCredentials(new OAuthFlow()
                    .tokenUrl(tokenUrl)
                    .scopes(new Scopes()))
            );
    }
}

