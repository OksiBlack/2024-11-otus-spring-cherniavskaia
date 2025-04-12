package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.otus.hw.security.JpaUserDetailsService;
import ru.otus.hw.security.JpaUserPasswordService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityFormLoginConfig {
    @Order(value = Ordered.HIGHEST_PRECEDENCE + 100)
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            AuthenticationManager authenticationManager
    ) throws Exception {
        http.securityMatcher("/**");
        http.authorizeHttpRequests(authorise ->
                authorise
                    .requestMatchers("/",
                        "/static/**", "/images/**",
                        "/swagger-ui/index.html",
                        "/swagger*/**",
                        "/v3*/api-docs/**",
                        "/actuator/**"
                    )
                    .permitAll()
                    .requestMatchers("authors/**", "genres/**", "books/**")
                    .hasAnyRole("READER", "EDITOR", "ADMIN")
                    .anyRequest()
                    .authenticated())
            .authenticationManager(authenticationManager);

        http.formLogin(withDefaults())
            .logout(httpSecurityLogoutConfigurer -> {
                httpSecurityLogoutConfigurer.logoutSuccessUrl("/");
            }).exceptionHandling(withDefaults());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider jpaAuthenticationProvider(JpaUserDetailsService jpaUserDetailsService,
                                                               PasswordEncoder passwordEncoder,
                                                               JpaUserPasswordService jpaUserPasswordService,
                                                               SimpleAuthorityMapper simpleAuthorityMapper
    ) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setAuthoritiesMapper(simpleAuthorityMapper);
        authProvider.setUserDetailsService(jpaUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        authProvider.setUserDetailsPasswordService(jpaUserPasswordService);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
        HttpSecurity http,
        DaoAuthenticationProvider jpaAuthenticationProvider
    ) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .authenticationProvider(jpaAuthenticationProvider)
            .build();
    }

    @Bean
    public SimpleAuthorityMapper simpleAuthorityMapper() {
        return new SimpleAuthorityMapper();
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
