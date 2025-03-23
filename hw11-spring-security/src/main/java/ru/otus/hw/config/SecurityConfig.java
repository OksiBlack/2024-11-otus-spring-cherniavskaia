package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true
)
class SecurityConfig {



//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            // Configures authorization rules for different endpoints
//            .authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/").permitAll() // Allows public access to the root URL
//                .requestMatchers("/menu").authenticated() // Requires authentication to access "/menu"
//                .anyRequest().authenticated() // Requires authentication for any other request
//            )
//            // Configures OAuth2 login settings
//            .oauth2Login(oauth2 -> oauth2
//                .loginPage("/oauth2/authorization/keycloak") // Sets custom login page for OAuth2 with Keycloak
//                .defaultSuccessUrl("/books/list", true) // Redirects to "/menu" after successful login
//            )
//            // Configures logout settings
//            .logout(logout -> logout
//                .logoutSuccessUrl("/") // Redirects to the root URL on successful logout
//                .invalidateHttpSession(true) // Invalidates session to clear session data
//                .clearAuthentication(true) // Clears authentication details
//                .deleteCookies("JSESSIONID") // Deletes the session cookie
//            )
//        ;
//
//        return http.build();
//    }
//    @Bean
//    SecurityFilterChain clientSecurityFilterChain(
//        HttpSecurity http,
//        ClientRegistrationRepository clientRegistrationRepository) throws Exception {
//        http.oauth2Login(Customizer.withDefaults());
//        http.logout((logout) -> {
//            var logoutSuccessHandler =
//                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
//            logoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/");
//            logout.logoutSuccessHandler(logoutSuccessHandler);
//        });
//
//        http.authorizeHttpRequests(requests -> {
//            requests.requestMatchers("/login", "/favicon.ico").permitAll();
/// /            requests.requestMatchers("/nice").hasAuthority("NICE");
//            requests.anyRequest().authenticated();
////            requests.anyRequest().denyAll();
//        });
//
//        return http.build();
//    }

//    @Bean
//    Converter realmRolesAuthoritiesConverter() {
//        return claims -> {
//            var realmAccess = Optional.ofNullable(((Map<String, Object>) claims).get("realm_access"));
//            var roles = realmAccess.flatMap(map -> Optional.ofNullable(((List<String>) map).get("roles")));
//            return roles.map(List::stream)
//                .orElse(Stream.empty())
//                .map(SimpleGrantedAuthority::new)
//                .map(GrantedAuthority.class::cast)
//                .toList();
//        };
//    }
//
//    @Bean
//    GrantedAuthoritiesMapper authenticationConverter(
//        Converter<Map<String, Object>, Collection<GrantedAuthority>> authoritiesConverter) {
//        return (authorities) -> authorities.stream()
//            .filter(authority -> authority instanceof OidcUserAuthority)
//            .map(OidcUserAuthority.class::cast)
//            .map(OidcUserAuthority::getIdToken)
//            .map(OidcIdToken::getClaims)
//            .map(authoritiesConverter::convert)
//            .flatMap(roles -> roles.stream())
//            .collect(Collectors.toSet());
//    }
//    private final com.baeldung.keycloak.KeycloakLogoutHandler keycloakLogoutHandler;
//
//    SecurityConfig(com.baeldung.keycloak.KeycloakLogoutHandler keycloakLogoutHandler) {
//        this.keycloakLogoutHandler = keycloakLogoutHandler;
//    }
//
//    @Bean
//    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests()
//            .antMatchers("/customers*")
//            .hasRole("USER")
//            .anyRequest()
//            .permitAll();
//        http.oauth2Login()
//            .and().logout()
//            .addLogoutHandler(keycloakLogoutHandler)
//            .logoutSuccessUrl("/");
//        http.oauth2ResourceServer(it->new OAuth2ResourceServerConfigurer());
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//            .build();
//    }
}