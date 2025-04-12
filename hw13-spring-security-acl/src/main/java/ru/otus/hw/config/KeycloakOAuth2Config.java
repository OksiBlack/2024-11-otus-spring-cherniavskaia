package ru.otus.hw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.otus.hw.security.keycloak.UsernameSubjectClaimAdapter;

//@RequiredArgsConstructor
@Configuration
public class KeycloakOAuth2Config {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

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

        http.csrf(AbstractHttpConfigurer::disable);
        http.exceptionHandling(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public KeycloakJwtAuthenticationConverter keycloakJwtAuthenticationConverter() {
        return new KeycloakJwtAuthenticationConverter();
    }

    @Bean
    public UsernameSubjectClaimAdapter usernameSubjectClaimAdapter() {
        return new UsernameSubjectClaimAdapter();
    }

    @Bean
    JwtDecoder jwtDecoder(UsernameSubjectClaimAdapter usernameSubjectClaimAdapter) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withIssuerLocation(issuer).build();
        jwtDecoder.setClaimSetConverter(usernameSubjectClaimAdapter);
        return jwtDecoder;
    }

}
