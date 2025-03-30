package ru.otus.hw.api.rest.controller.management;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.model.management.Authority;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/management/authorities")
public class AuthorityController {

    private final ru.otus.hw.repository.management.AuthorityRepository authorityRepository;

    @GetMapping
    public List<Authority> getAllAuthorities() {
        return authorityRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Authority> getAuthorityById(@PathVariable Long id) {
        Optional<Authority> authority = authorityRepository.findById(id);
        return authority.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Authority> createAuthority(@RequestBody Authority authority) {
        Authority savedAuthority = authorityRepository.save(authority);
        return new ResponseEntity<>(savedAuthority, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Authority> updateAuthority(@PathVariable Long id, @RequestBody Authority authority) {
        if (!authorityRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        authority.setId(id);
        Authority updatedAuthority = authorityRepository.save(authority);
        return ResponseEntity.ok(updatedAuthority);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthority(@PathVariable Long id) {
        if (!authorityRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        authorityRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
