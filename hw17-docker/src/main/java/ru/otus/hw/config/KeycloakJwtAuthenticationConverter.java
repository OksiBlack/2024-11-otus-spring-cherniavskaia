package ru.otus.hw.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String ROLE_AUTHORITY_PREFIX = "ROLE_";

    private static final String SCOPE_PREFIX = "SCOPE_";

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(final Jwt source) {
        String authorizedParty = source.getClaimAsString(Claims.AZP_CLAIM);
        Collection<GrantedAuthority> grantedAuthorities = jwtGrantedAuthoritiesConverter.convert(source);
        Collection<? extends GrantedAuthority> extractResourceRoles = extractResourceRoles(source, authorizedParty);
        Collection<? extends GrantedAuthority> extractRealmRoles = extractRealmRoles(source);

        Set<GrantedAuthority> allGrantedAuthorities = new LinkedHashSet<>();
        allGrantedAuthorities.addAll(grantedAuthorities);
        allGrantedAuthorities.addAll(extractRealmRoles);
        allGrantedAuthorities.addAll(extractResourceRoles);
        allGrantedAuthorities.addAll(extractScopes(source));
        return new JwtAuthenticationToken(source, allGrantedAuthorities);
    }

    private Collection<? extends GrantedAuthority> extractScopes(final Jwt jwt) {
        String scope = jwt.getClaimAsString(Claims.SCOPE_CLAIM);
        if (scope != null) {
            List<String> scopes = Arrays.asList(scope.split("\\s+"));
            return scopes.stream()
                .map(x -> new SimpleGrantedAuthority(SCOPE_PREFIX + x))
                .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    @SuppressWarnings("unchecked")
    private Collection<? extends GrantedAuthority> extractRealmRoles(final Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim(Claims.REALM_ACCESS_CLAIM);
        Collection<String> resourceRoles;
        if (realmAccess != null) {
            resourceRoles = (Collection<String>) realmAccess.get(Claims.ROLES_CLAIM);
            return resourceRoles.stream()
                .map(x -> new SimpleGrantedAuthority(ROLE_AUTHORITY_PREFIX + x))
                .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    @SuppressWarnings("unchecked")
    private Collection<? extends GrantedAuthority> extractResourceRoles(final Jwt jwt, final String resourceId) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        Map<String, Object> resource;
        Collection<String> resourceRoles;
        if (resourceAccess != null && (resource = (Map<String, Object>) resourceAccess.get(resourceId)) != null &&
            (resourceRoles = (Collection<String>) resource.get(Claims.ROLES_CLAIM)) != null) {
            return resourceRoles.stream()
                .map(x -> new SimpleGrantedAuthority(ROLE_AUTHORITY_PREFIX + x))
                .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    private static class Claims {
        private static final String AZP_CLAIM = "azp";

        private static final String ROLES_CLAIM = "roles";

        private static final String REALM_ACCESS_CLAIM = "realm_access";

        private static final String SCOPE_CLAIM = "scope";

    }
}