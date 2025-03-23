package ru.otus.hw.security;


import ru.otus.hw.model.management.Authority;

import java.util.List;

public interface AuthorityService {
    List<Authority> getAllAuthorities();
    Authority getAuthorityById(Long id);
    Authority createAuthority(Authority authority);
    Authority updateAuthority(Long id, Authority authority);
    void deleteAuthority(Long id);
}
