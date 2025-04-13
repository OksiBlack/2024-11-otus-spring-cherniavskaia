package ru.otus.hw.security.keycloak;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientMappingsRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.KeycloakProperties;
import ru.otus.hw.exception.KeycloakException;
import ru.otus.hw.model.management.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.PASSWORD;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class KeycloakUserService implements UserDetailsManager {

    private final KeycloakProperties keycloakProperties;

    private final KeycloakUserMapper keycloakUserMapper;

    private final SecurityContextHolderStrategy securityContextHolderStrategy =
        SecurityContextHolder.getContextHolderStrategy();

    private Keycloak createKeycloakClient() {
        KeycloakProperties.Admin admin = keycloakProperties.getAdmin();
        return KeycloakBuilder.builder()
            .serverUrl(keycloakProperties.getServer().getBaseUrl())
            .realm(keycloakProperties.getRealm())
            .grantType(PASSWORD)
            .clientId(admin.getClientId())
            .username(admin.getUsername())
            .password(admin.getPassword())
            .build();
    }

    private RealmResource getRealmResource(Keycloak keycloak) {
        return keycloak.realm(keycloakProperties.getRealm());
    }

    private List<UserRepresentation> listUsersWithRoles() {
        Keycloak keycloak = createKeycloakClient();
        UsersResource usersResource = getRealmResource(keycloak).users();

        // Fetch users based on username and populate roles
        List<UserRepresentation> userRepresentations = usersResource.list();
        userRepresentations.forEach(user -> {
            populateRolesForUser(usersResource, user);
        });

        return userRepresentations;
    }

    public List<UserRepresentation> getUsersWithRoles(String username) {
        Keycloak keycloak = createKeycloakClient();
        UsersResource usersResource = getRealmResource(keycloak).users();

        // Fetch users based on username and populate roles
        List<UserRepresentation> userRepresentations = usersResource.searchByUsername(username, false);
        userRepresentations.forEach(user -> {
            populateRolesForUser(usersResource, user);
        });

        return userRepresentations;
    }

    private void populateRolesForUser(UsersResource usersResource, UserRepresentation user) {
        List<RoleRepresentation> roles = getRealmRolesForUser(usersResource, user.getId());
        user.setRealmRoles(getRoleNames(roles));
        Map<String, ClientMappingsRepresentation> clientRolesForUser = getClientRolesForUser(
            usersResource, user.getId()
        );
        Map<String, List<String>> clientRoles = mapClientRoles(clientRolesForUser);
        user.setClientRoles(clientRoles);
    }

    private Map<String, List<String>> mapClientRoles(Map<String, ClientMappingsRepresentation> clientRolesForUser) {
        if (clientRolesForUser == null) {
            return Collections.emptyMap();
        }
        Map<String, List<String>> clientRoles = new HashMap<>();
        clientRolesForUser.forEach((key, value) -> {
            clientRoles.put(key, value.getMappings().stream().map(RoleRepresentation::getName).toList());
        });
        return clientRoles;
    }

    private List<String> getRoleNames(List<RoleRepresentation> roles) {
        return roles.stream().map(RoleRepresentation::getName).toList();
    }

    private List<RoleRepresentation> getRealmRolesForUser(UsersResource usersResource, String userId) {
        return getRolesSafely(() -> usersResource.get(userId).roles().getAll().getRealmMappings());
    }

    private Map<String, ClientMappingsRepresentation> getClientRolesForUser(UsersResource usersResource, String userId) {
        return getRolesSafely(() -> usersResource.get(userId).roles().getAll().getClientMappings());
    }

    private <T> T getRolesSafely(RiskyOperation<T> operation) {
        try {
            return operation.execute();
        } catch (Exception e) {
            throw new KeycloakException(e);
        }
    }

    @Override
    public void createUser(UserDetails user) {
        validateUserInstance(user);
        User bookStoreUser = (User) user;

        UserRepresentation userRepresentation = mapUserToRepresentation(bookStoreUser);
        userRepresentation.setCredentials(List.of(createCredential(user.getPassword())));

        Response response = getRealmResource(createKeycloakClient()).users()
            .create(userRepresentation);
        log.info("User creation response: {}", response);
    }

    private void validateUserInstance(UserDetails user) {
        if (!(user instanceof User)) {
            throw new IllegalArgumentException("Wrong user type provided. Expecting [%s]."
                .formatted(User.class.getName()));
        }
    }

    private UserRepresentation updateUserRepresentation(UserRepresentation userRepresentation, User bookStoreUser) {
        userRepresentation.setId(Optional.ofNullable(bookStoreUser.getId()).map(UUID::toString)
            .orElse(UUID.randomUUID().toString()));
        userRepresentation.setUsername(bookStoreUser.getUsername());
        userRepresentation.setFirstName(bookStoreUser.getFirstName());
        userRepresentation.setLastName(bookStoreUser.getLastName());
        userRepresentation.setEmail(bookStoreUser.getEmail());
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);
        return userRepresentation;
    }

    private UserRepresentation mapUserToRepresentation(User bookStoreUser) {
        UserRepresentation userRepresentation = new UserRepresentation();
        return updateUserRepresentation(userRepresentation, bookStoreUser);
    }

    private CredentialRepresentation createCredential(String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        return credential;
    }

    private Authentication createNewAuthentication(UserDetails user) {
        UsernamePasswordAuthenticationToken newAuth = UsernamePasswordAuthenticationToken
            .authenticated(user, null, user.getAuthorities());
        newAuth.setDetails(user);
        return newAuth;
    }

    @Override
    public void updateUser(UserDetails user) {
        validateUserInstance(user);
        User bookStoreUser = (User) user;

        UserResource userResource = getRealmResource(createKeycloakClient())
            .users()
            .get(bookStoreUser.getId().toString());

        UserRepresentation representation = userResource.toRepresentation();

        userResource.update(updateUserRepresentation(representation, bookStoreUser));
    }

    @Override
    public void deleteUser(String username) {
        getRealmResource(createKeycloakClient()).users().delete(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
        validateCurrentUser(currentUser);

        String username = currentUser.getName();
        log.debug("Changing password for user [{}]", username);

        UserDetails userDetails = loadUserByUsername(username);
        validateOldPassword(userDetails, oldPassword);

        UserResource userResource = getRealmResource(createKeycloakClient()).users().get(userDetails.getUsername());
        userResource.resetPassword(createCredential(oldPassword));
        userResource.update(createNewPasswordRepresentation(userResource, newPassword));

        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(createNewAuthentication(userDetails));
    }

    private void validateCurrentUser(Authentication currentUser) {
        if (currentUser == null) {
            throw new AccessDeniedException("Authentication object not found in context for current user.");
        }
    }

    private void validateOldPassword(UserDetails userDetails, String oldPassword) {
        if (!Objects.equals(userDetails.getPassword(), oldPassword)) {
            throw new AccessDeniedException(
                "Old password mismatch for current user [%s]"
                    .formatted(userDetails.getUsername())
            );
        }
    }

    private UserRepresentation createNewPasswordRepresentation(UserResource userResource, String newPassword) {
        UserRepresentation representation = userResource.toRepresentation();
        representation.setCredentials(List.of(createCredential(newPassword)));
        return representation;
    }

    @Override
    public boolean userExists(String username) {
        return !getRealmResource(createKeycloakClient()).users().search(username).isEmpty();
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = getUsersWithRoles(username)
            .stream()
            .map(keycloakUserMapper::toUser)
            .toList();

        if (users.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return users.get(0);
    }

    public List<User> getAllUsers() {
        return listUsersWithRoles().stream()
            .map(keycloakUserMapper::toUser)
            .toList();
    }

}
