package ru.otus.hw.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.model.management.User;
import ru.otus.hw.repository.management.UserRepository;
import ru.otus.hw.repository.spec.UserSpecifications;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class JpaUserDetailsService implements JpaUserDetailsManager {
    private final UserRepository userRepository;

    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
        .getContextHolderStrategy();

    @Transactional
    @Override
    public void createUser(UserDetails user) {
        User bookStoreUser;
        if (!(user instanceof User)) {
            throw new IllegalArgumentException("Wrong user type provided. Expecting [%s]."
                .formatted(User.class.getName()));
        }

        bookStoreUser = (User) user;
        userRepository.save(bookStoreUser);
    }

    @Transactional
    @Override
    public void updateUser(UserDetails user) {
        User bookStoreUser;
        if (!(user instanceof User)) {
            throw new IllegalArgumentException("Wrong user type provided. Expecting [%s]."
                .formatted(User.class.getName()));
        }

        bookStoreUser = (User) user;

        userRepository.save(bookStoreUser);
    }

    @Transactional
    @Override
    public void deleteUser(String username) {
        userRepository.delete(UserSpecifications.hasLogin(username));
    }

    @Transactional
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = this.securityContextHolderStrategy.getContext().getAuthentication();
        if (currentUser == null) {
            // This would indicate bad coding somewhere
            throw new AccessDeniedException(
                "Can not change password as no Authentication object found in context for current user.");
        }
        String username = currentUser.getName();

        log.debug("Changing password for user [{}]", username);

        User userDetails = loadUserByUsername(username);
        if (!Objects.equals(userDetails.getPassword(), oldPassword)) {
            throw new AccessDeniedException("Old password mismatch for current user [%s]".formatted(username));
        }

        userDetails.setPassword(newPassword);

        Authentication authentication = createNewAuthentication(userDetails);
        SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authentication);
        this.securityContextHolderStrategy.setContext(context);
    }

    private Authentication createNewAuthentication(User user) {
        UsernamePasswordAuthenticationToken newAuthentication = UsernamePasswordAuthenticationToken.authenticated(user,
            null, user.getAuthorities());
        newAuthentication.setDetails(user);
        return newAuthentication;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean userExists(String username) {
        return userRepository.findOne(UserSpecifications.hasLogin(username)).isPresent();
    }

    @Transactional(readOnly = true)
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findOne(UserSpecifications.hasLogin(username)).orElseThrow(() ->
            new UsernameNotFoundException("No user with login: [%s]".formatted(username)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User getById(Long id) {
        return userRepository.findOne(UserSpecifications.hasId(id)).orElseThrow(() ->
            new EntityNotFoundException("No user with id: [%s]".formatted(id)));
    }
}
