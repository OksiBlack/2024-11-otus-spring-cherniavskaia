package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class KeycloakOAuth2Config {

    @Order(value = Ordered.HIGHEST_PRECEDENCE)
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

        http.csrf(Customizer.withDefaults());
        http.exceptionHandling(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter() {
        return new KeycloakJwtAuthenticationConverter();
    }
}
