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
import ru.otus.hw.api.rest.controller.management.dto.AuthorityDto;
import ru.otus.hw.api.rest.controller.management.dto.AuthorityRequest;
import ru.otus.hw.model.management.Authority;
import ru.otus.hw.security.AuthorityService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/management/authorities")
public class AuthorityController {

    private final AuthorityService authorityService;

    @GetMapping
    public List<Authority> getAllAuthorities() {
        return authorityService.getAllAuthorities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Authority> getAuthorityById(@PathVariable UUID id) {
        Authority authority = authorityService.getAuthorityById(id);

        return new ResponseEntity<>(authority, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AuthorityDto> createAuthority(@RequestBody AuthorityRequest authorityRequest) {
        Authority authority = new Authority();
        authority.setName(authorityRequest.getName());
        authority.setDescription(authorityRequest.getDescription());

        Authority savedAuthority = authorityService.createAuthority(authority);

        AuthorityDto name = mapToAuthorityDto(savedAuthority);

        return new ResponseEntity<>(name, HttpStatus.CREATED);
    }

    private AuthorityDto mapToAuthorityDto(Authority savedAuthority) {
        return AuthorityDto.builder()
            .id(savedAuthority.getId())
            .name(savedAuthority.getName())
            .description(savedAuthority.getDescription())
            .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorityDto> updateAuthority(
        @PathVariable UUID id,
        @RequestBody AuthorityRequest authorityRequest) {
        if (!authorityService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Authority authority = new Authority();
        authority.setId(id);
        authority.setName(authorityRequest.getName());
        authority.setDescription(authorityRequest.getDescription());

        Authority updatedAuthority = authorityService.updateAuthority(id, authority);
        return ResponseEntity.ok(mapToAuthorityDto(updatedAuthority));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthority(@PathVariable UUID id) {
        if (!authorityService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        authorityService.deleteAuthority(id);
        return ResponseEntity.noContent().build();
    }
}
