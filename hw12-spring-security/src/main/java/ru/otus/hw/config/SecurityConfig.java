package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.otus.hw.security.JpaUserDetailsService;
import ru.otus.hw.security.JpaUserPasswordService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true
)
class SecurityConfig {


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.authorizeHttpRequests(authorise ->
                authorise
                    .requestMatchers("/",
                        "/static/**",
                        "/images/**",
                        "/swagger-ui/index.html",
                        "/swagger*/**",
                        "/v3*/api-docs",
                        "actuator/**"
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
            });

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
    public AuthenticationManager authenticationManager(HttpSecurity http, DaoAuthenticationProvider jpaAuthenticationProvider) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .authenticationProvider(jpaAuthenticationProvider)
            .build();
    }

    @Bean
    public SimpleAuthorityMapper simpleAuthorityMapper() {
        return new SimpleAuthorityMapper();
    }

//    @Bean
//    public static JpaUserDetailsService jpaUserDetailsService(PasswordEncoder passwordEncoder,
//                                                              UserRepository userRepository,
//                                                              AuthorityRepository authorityRepository,
//                                                              SimpleAuthorityMapper simpleAuthorityMapper
//
//    ) {
//        return new JpaUserDetailsService(passwordEncoder, userRepository, authorityRepository, simpleAuthorityMapper);
//    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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