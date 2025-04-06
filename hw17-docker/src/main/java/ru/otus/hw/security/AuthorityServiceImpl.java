package ru.otus.hw.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.management.Authority;
import ru.otus.hw.repository.management.AuthorityRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Override
    public List<Authority> getAllAuthorities() {
        return authorityRepository.findAll();
    }

    @Override
    public Authority getAuthorityById(Long id) {
        return authorityRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Authority with id " + id + " not found"));
    }

    @Override
    public Authority createAuthority(Authority authority) {
        return authorityRepository.save(authority);
    }

    @Override
    public Authority updateAuthority(Long id, Authority authority) {
        if (!authorityRepository.existsById(id)) {
            throw new EntityNotFoundException("Authority with id " + id + " not found");
        }
        authority.setId(id);
        return authorityRepository.save(authority);
    }

    @Override
    public void deleteAuthority(Long id) {
        if (!authorityRepository.existsById(id)) {
            throw new EntityNotFoundException("Authority with id " + id + " not found");
        }
        authorityRepository.deleteById(id);
    }
}
