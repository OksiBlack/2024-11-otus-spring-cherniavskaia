package ru.otus.hw.api.rest.controller.management.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;

    private String login;

    private String firstName;

    private String lastName;

    private String middleName;

    private LocalDate birthday;

    private Set<String> roles;
}
