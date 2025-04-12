package ru.otus.hw.security.keycloak;

import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.management.Authority;

import java.util.Optional;
import java.util.UUID;

@Service
public class KeycloakRoleAuthorityMapper implements RoleAuthorityMapper {

    @Override
    public RoleRepresentation toRoleRepresentation(Authority authority) {
        if (authority == null) {
            return null;
        }

        RoleRepresentation roleRepresentation = new RoleRepresentation();
        roleRepresentation.setId(authority.getId().toString());
        roleRepresentation.setName(authority.getName());
        roleRepresentation.setDescription(authority.getDescription());

        roleRepresentation.setComposite(false);
        roleRepresentation.setClientRole(false);

        return roleRepresentation;
    }

    @Override
    public Authority toAuthority(RoleRepresentation roleRepresentation) {
        if (roleRepresentation == null) {
            return null;
        }

        Authority authority = new Authority();
        authority.setId(Optional.ofNullable(roleRepresentation.getId())
            .map(UUID::fromString).orElse(null));
        authority.setName(roleRepresentation.getName());
        authority.setDescription(roleRepresentation.getDescription());

        return authority;
    }
}
