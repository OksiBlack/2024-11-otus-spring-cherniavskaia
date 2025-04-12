package ru.otus.hw.security.keycloak;

import org.keycloak.representations.idm.RoleRepresentation;
import ru.otus.hw.model.management.Authority;

public interface RoleAuthorityMapper {
    RoleRepresentation toRoleRepresentation(Authority authority);

    Authority toAuthority(RoleRepresentation roleRepresentation);
}
