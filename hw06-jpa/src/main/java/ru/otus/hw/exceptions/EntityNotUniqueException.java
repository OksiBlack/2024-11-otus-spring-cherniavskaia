package ru.otus.hw.exceptions;

public class EntityNotUniqueException extends RuntimeException {
    public EntityNotUniqueException(String message) {
        super(message);
    }
}
