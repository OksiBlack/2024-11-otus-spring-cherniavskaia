package ru.otus.hw.api.rest.controller.management.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserDto {
    private Long id;

    private String login;

    private String firstName;

    private String lastName;

    private String middleName;

    private LocalDate birthday;

    private Set<String> roles; // Returning role names
}
