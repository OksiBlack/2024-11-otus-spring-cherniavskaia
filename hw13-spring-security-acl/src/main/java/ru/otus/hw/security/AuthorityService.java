package ru.otus.hw.security;


import ru.otus.hw.model.management.Authority;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface AuthorityService {
    List<Authority> getAllAuthorities();

    Authority getAuthorityById(UUID id);

    Set<Authority> getAuthoritiesById(Set<UUID> ids);

    Authority createAuthority(Authority authority);

    Authority updateAuthority(UUID id, Authority authority);

    void deleteAuthority(UUID id);

    boolean existsById(UUID id);
}
