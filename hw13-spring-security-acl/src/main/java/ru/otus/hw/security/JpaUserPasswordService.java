package ru.otus.hw.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.management.User;
import ru.otus.hw.repository.management.UserRepository;

@Service
@RequiredArgsConstructor
public class JpaUserPasswordService implements UserDetailsPasswordService {
    private final UserRepository userRepository;

    @Override
    public User updatePassword(UserDetails user, String newPassword) {

        User bookStoreUser = userRepository.findByLogin(user.getUsername()).orElseThrow(
            () -> new EntityNotFoundException("User with login [] not found")
        );

        bookStoreUser.setPassword(newPassword);
        userRepository.save(bookStoreUser);
        return bookStoreUser;
    }
}
