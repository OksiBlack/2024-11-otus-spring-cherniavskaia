package ru.otus.hw.security.keycloak;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.management.Authority;
import ru.otus.hw.model.management.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class KeycloakUserMapper implements UserMapper {

    // Method to convert UserRepresentation to User
    @Override
    public User toUser(UserRepresentation userRepresentation) {
        if (userRepresentation == null) {
            return null;
        }

        return User.builder()
            .id(userRepresentation.getId() != null ? UUID.fromString(userRepresentation.getId()) : null)
            .login(userRepresentation.getUsername())
            .firstName(userRepresentation.getFirstName())
            .lastName(userRepresentation.getLastName())
            .password(getPassword(userRepresentation.getCredentials()))
            .email(userRepresentation.getEmail())
            .roles(convertRoles(userRepresentation))
            .build();
    }

    // Method to convert User to UserRepresentation
    @Override
    public UserRepresentation toUserRepresentation(User user) {
        if (user == null) {
            return null;
        }
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(String.valueOf(user.getId())); // Assuming user.getId() gives Long id
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType("password");
        credential.setValue(user.getPassword());
        userRepresentation.setCredentials(List.of(credential));

        userRepresentation.setRealmRoles(user.getRoles()
            .stream()
            .map(Authority::getName) // Assuming Authority has a getName() method
            .toList());

        return userRepresentation;
    }

    private String getPassword(List<CredentialRepresentation> credentials) {
        return credentials != null && !credentials.isEmpty() ?
            credentials.get(0).getValue() : null; // Assuming you only need the first password
    }

    private Set<Authority> convertRoles(UserRepresentation userRepresentation) {
        Map<String, List<String>> clientRoles = userRepresentation.getClientRoles();
        List<String> realmRoles = userRepresentation.getRealmRoles();

        // You'll need to create a mapping of roles based on how they are stored in your system
        // This is just an example; adapt as necessary
        Set<Authority> authorities = realmRoles.stream()
            .map(roleName -> {
                Authority authority = new Authority();
                authority.setName(roleName);
                return authority;
            }).collect(Collectors.toSet());

        Set<Authority> clientAuthorities = clientRoles.values().stream()
            .flatMap(Collection::stream).map(role -> {
                Authority authority = new Authority();
                authority.setName(role);
                return authority;
            }).collect(Collectors.toSet());

        authorities.addAll(clientAuthorities);
        return authorities;
    }
}
