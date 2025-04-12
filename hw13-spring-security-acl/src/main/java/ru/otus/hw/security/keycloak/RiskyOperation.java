package ru.otus.hw.security.keycloak;

@FunctionalInterface
interface RiskyOperation<T> {
    T execute() throws RuntimeException;
}
