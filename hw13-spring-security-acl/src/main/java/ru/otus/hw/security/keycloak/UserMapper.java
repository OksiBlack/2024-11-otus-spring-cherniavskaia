package ru.otus.hw.security.keycloak;

import org.keycloak.representations.idm.UserRepresentation;
import ru.otus.hw.model.management.User;

public interface UserMapper {
    User toUser(UserRepresentation userRepresentation);

    UserRepresentation toUserRepresentation(User user);
}
