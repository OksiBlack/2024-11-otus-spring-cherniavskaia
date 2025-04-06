package ru.otus.hw.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    proxyTargetClass = true
)
@Import(value = {SecurityFormLoginConfig.class, KeycloakOAuth2Config.class})
class SecurityConfig {

}