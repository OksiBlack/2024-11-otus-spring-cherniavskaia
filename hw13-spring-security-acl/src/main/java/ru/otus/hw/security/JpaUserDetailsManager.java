package ru.otus.hw.security;

import org.springframework.security.provisioning.UserDetailsManager;
import ru.otus.hw.model.management.User;

import java.util.List;

public interface JpaUserDetailsManager extends UserDetailsManager {
     List<User> getAllUsers();

     User getById(Long id);
}
