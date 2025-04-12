package ru.otus.hw.security.keycloak;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.KeycloakProperties;
import ru.otus.hw.model.management.Authority;
import ru.otus.hw.security.AuthorityService;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.PASSWORD;

@Primary
@Service
@RequiredArgsConstructor
public class KeycloakAuthorityService implements AuthorityService {
    private final KeycloakProperties keycloakProperties;

    private final KeycloakUserMapper keycloakUserMapper;

    private final KeycloakRoleAuthorityMapper keycloakRoleAuthorityMapper;

    private Keycloak createKeycloakClient() {
        return KeycloakBuilder.builder()
            .serverUrl(keycloakProperties.getServer().getBaseUrl())
            .realm(keycloakProperties.getRealm())
            .grantType(PASSWORD)
            .clientId(keycloakProperties.getAdmin().getClientId())
            .username(keycloakProperties.getAdmin().getUsername())
            .password(keycloakProperties.getAdmin().getPassword())
            .build();
    }

    private RealmResource getRealmResource(Keycloak keycloak) {
        return keycloak.realm(keycloakProperties.getRealm());
    }

    @Override
    public List<Authority> getAllAuthorities() {
        return getRealmResource().roles().list()
            .stream().map(this::mapRoleToAuthority)
            .toList();
    }

    private Authority mapRoleToAuthority(RoleRepresentation roleRepresentation) {
        return keycloakRoleAuthorityMapper.toAuthority(roleRepresentation);
    }

    private RealmResource getRealmResource() {
        return getRealmResource(createKeycloakClient());
    }

    @Override
    public Authority getAuthorityById(UUID id) {
        RoleRepresentation role = getRealmResource(createKeycloakClient())
            .rolesById().getRole(id.toString());
        return keycloakRoleAuthorityMapper.toAuthority(role);
    }

    @Override
    public Set<Authority> getAuthoritiesById(Set<UUID> ids) {
        return getRealmResource(createKeycloakClient()).roles()
            .list().stream().filter(role -> ids.contains(UUID.fromString(role.getId())))
            .map(this::mapRoleToAuthority).collect(Collectors.toSet());
    }

    @Override
    public Authority createAuthority(Authority authority) {
        RoleRepresentation roleRepresentation = new RoleRepresentation(
            authority.getName(),
            authority.getDescription(), false
        );

        roleRepresentation.setId(getId(authority));
        getRealmResource().roles().create(roleRepresentation);

        return keycloakRoleAuthorityMapper.toAuthority(roleRepresentation);
    }

    private String getId(Authority authority) {
        return authority.getId() != null ? authority.getId().toString() : UUID.randomUUID().toString();
    }

    @Override
    public Authority updateAuthority(UUID id, Authority authority) {
        RoleRepresentation representation = new RoleRepresentation(authority.getName(),
            authority.getName(),
            false
        );
        representation.setId(id.toString());
        getRealmResource().rolesById().updateRole(id.toString(), representation);

        return keycloakRoleAuthorityMapper.toAuthority(representation);
    }

    @Override
    public void deleteAuthority(UUID id) {
        getRealmResource().rolesById().deleteRole(id.toString());
    }

    @Override
    public boolean existsById(UUID id) {
        return getRealmResource().roles().list(false)
            .stream().anyMatch(it -> it.getId().equals(id.toString()));
    }
}
