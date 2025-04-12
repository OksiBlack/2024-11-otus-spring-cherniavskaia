package ru.otus.hw.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.hw.exception.EntityNotFoundException;


@Slf4j
@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(EntityNotFoundException.class)
    public String notFoundExceptionHandler() {
        return "error/404";
    }

    @ExceptionHandler(RuntimeException.class)
    public String exceptionHandler(Exception ex) {
        log.error("Error: [{}]", ex.getMessage());
        return "error/500";
    }
}
