package ru.otus.hw.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
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

@io.swagger.v3.oas.annotations.security.SecurityScheme(name = "oauth2", type = SecuritySchemeType.OAUTH2,
    flows = @io.swagger.v3.oas.annotations.security.OAuthFlows(clientCredentials = @io.swagger.v3.oas.annotations.security.OAuthFlow(tokenUrl = "${spring.security.oauth2.client.provider.oauth2-provider.token-uri}")))
@Configuration
public class SpringDocConfig {

    @Value("${spring.security.oauth2.client.provider.keycloak.token-uri}")
    private String tokenUrl;

    @Value("${spring.security.oauth2.client.provider.keycloak.authorization-uri}")
    private String authorizationUrl;

    @Bean
    public OpenAPI bookStoreOpenApi() {
        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement().addList("apiKey"))
            .addSecurityItem(new SecurityRequirement().addList("bearerToken"))
            .addSecurityItem(new SecurityRequirement().addList("authorizationCode"))
            .addSecurityItem(new SecurityRequirement().addList("clientCredentials"))
            .components(new Components()
                .addSecuritySchemes("apiKey",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name("X-API-KEY")
                )
                .addSecuritySchemes("bearerToken",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
                .addSecuritySchemes("authorizationCode",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.OAUTH2)
                        .flows(new OAuthFlows()
                            .authorizationCode(new OAuthFlow()
                                .authorizationUrl(authorizationUrl)
                                .tokenUrl(tokenUrl)
                            .scopes(new Scopes()
                                .addString("openid", "openid access")
                                .addString("profile", "profile access")
                                .addString("read", "Read access")
                                .addString("write", "Write access")))
                        )
                )
                .addSecuritySchemes("clientCredentials",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.OAUTH2)
                        .flows(new OAuthFlows()
                            .clientCredentials(new OAuthFlow()
                                .tokenUrl(tokenUrl)
                                .scopes(new Scopes()
                                    .addString("openid", "openid access")
                                    .addString("profile", "profile access")
                                    .addString("read", "Read access")
                                    .addString("write", "Write access")))
                        )
                )
            )
            .info(new Info().title("Book Store API").version("1.0.0"));
    }
}

