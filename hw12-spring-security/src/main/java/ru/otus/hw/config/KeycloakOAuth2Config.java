package ru.otus.hw.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class KeycloakOAuth2Config {

    @Order(value = SecurityProperties.BASIC_AUTH_ORDER - 200)
    @Bean
    public SecurityFilterChain jwtSecurityFilterChain(
        HttpSecurity http,
        KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter
    ) throws Exception {
        http.securityMatcher("/api/**");
        http.authorizeHttpRequests((requests) ->

            requests.requestMatchers("/api/management/**").hasRole("ADMIN")
                .requestMatchers("/api/**")
                .authenticated()
        );

        http.oauth2ResourceServer((resourceServer) -> {
                resourceServer.jwt(jwt -> {
                        jwt.jwtAuthenticationConverter(keycloakJwtAuthenticationConverter);
                    }
                );
            }
        );

        return http.build();
    }

    @Bean
    public KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter() {
        return new KeycloakJwtAuthenticationConverter();
    }

}
