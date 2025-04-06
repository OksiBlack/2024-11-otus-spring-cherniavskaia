package ru.otus.hw.api.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.otus.hw.exception.EntityNotFoundException;

import java.net.URI;

@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
            ex.getMessage());
        problemDetail.setType(URI.create("BOOKSTORE_REST_API"));

        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ProblemDetail> authorizationDeniedException(AuthorizationDeniedException ex) {
        log.error(ex.getMessage(), ex);
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN,
            ex.getMessage());
        problemDetail.setType(URI.create("BOOKSTORE_REST_API"));

        return new ResponseEntity<>(problemDetail, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(Exception ex) {
        log.error(ex.getMessage(), ex);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
            ex.getMessage());
        problemDetail.setType(URI.create("BOOKSTORE_REST_API"));
        return new ResponseEntity<>(problemDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
