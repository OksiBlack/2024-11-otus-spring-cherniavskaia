package ru.otus.hw.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.management.Authority;
import ru.otus.hw.repository.management.AuthorityRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Override
    public List<Authority> getAllAuthorities() {
        return authorityRepository.findAll();
    }

    @Override
    public Authority getAuthorityById(UUID id) {
        return authorityRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Authority with id %s not found".formatted(id)));
    }

    @Override
    public Set<Authority> getAuthoritiesById(Set<UUID> ids) {
        return new HashSet<>(authorityRepository.findAllById(ids));
    }

    @Override
    public Authority createAuthority(Authority authority) {
        return authorityRepository.save(authority);
    }

    @Override
    public Authority updateAuthority(UUID id, Authority authority) {
        if (!authorityRepository.existsById(id)) {
            throw new EntityNotFoundException("Authority with id %s not found".formatted(id));
        }
        authority.setId(id);
        return authorityRepository.save(authority);
    }

    @Override
    public void deleteAuthority(UUID id) {
        if (!authorityRepository.existsById(id)) {
            throw new EntityNotFoundException("Authority with id " + id + " not found");
        }
        authorityRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return authorityRepository.existsById(id);
    }
}
