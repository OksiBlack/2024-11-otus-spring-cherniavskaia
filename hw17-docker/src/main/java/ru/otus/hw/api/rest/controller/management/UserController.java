package ru.otus.hw.api.rest.controller.management;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.api.rest.controller.management.dto.UserCreateRequest;
import ru.otus.hw.api.rest.controller.management.dto.UserDto;
import ru.otus.hw.api.rest.controller.management.dto.UserUpdateRequest;
import ru.otus.hw.model.management.Authority;
import ru.otus.hw.model.management.User;
import ru.otus.hw.security.AuthorityService;
import ru.otus.hw.security.JpaUserDetailsService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/management/users")
public class UserController {

    private final JpaUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityService authorityService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userDetailsService.getAllUsers().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    @GetMapping("/{login}")
    public ResponseEntity<UserDto> getUserByLogin(@PathVariable String login) {
        User user = (User) userDetailsService.loadUserByUsername(login);
        return ResponseEntity.ok(convertToDto(user));
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserCreateRequest userCreateRequest) {
        User user = new User();
        user.setLogin(userCreateRequest.getLogin());
        user.setFirstName(userCreateRequest.getFirstName());
        user.setLastName(userCreateRequest.getLastName());
        user.setMiddleName(userCreateRequest.getMiddleName());
        user.setBirthday(userCreateRequest.getBirthday());
        user.setPassword(passwordEncoder.encode(userCreateRequest.getPassword()));

        user.setRoles(userCreateRequest.getRoleIds().stream()
            .map(authorityService::getAuthorityById)
            .collect(Collectors.toSet()));

        userDetailsService.createUser(user);

        return new ResponseEntity<>(convertToDto(user), HttpStatus.CREATED);
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @PatchMapping("/{login}")
    public ResponseEntity<UserDto> updateUser(
        @PathVariable String login,
        @RequestBody UserUpdateRequest userUpdateRequest
    ) {
        User existingUser = (User) userDetailsService.loadUserByUsername(login);

        existingUser.setFirstName(userUpdateRequest.getFirstName());
        existingUser.setLastName(userUpdateRequest.getLastName());
        existingUser.setMiddleName(userUpdateRequest.getMiddleName());
        existingUser.setBirthday(userUpdateRequest.getBirthday());
        existingUser.setRoles(userUpdateRequest.getRoleIds().stream()
            .map(authorityService::getAuthorityById)
            .collect(Collectors.toSet()));

        userDetailsService.updateUser(existingUser);

        return ResponseEntity.ok(convertToDto(existingUser));
    }

    @PreAuthorize(value = "hasRole('ADMIN')")
    @DeleteMapping("/{login}")
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        if (!userDetailsService.userExists(login)) {
            return ResponseEntity.notFound().build();
        }
        userDetailsService.deleteUser(login);
        return ResponseEntity.noContent().build();
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setMiddleName(user.getMiddleName());
        dto.setBirthday(user.getBirthday());
        dto.setRoles(user.getRoles().stream()
            .map(Authority::getName)
            .collect(Collectors.toSet()));
        return dto;
    }
}