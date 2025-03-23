package ru.otus.hw.api.rest.controller.management.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class UserCreateRequest {
    private String login;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthday;
    private String password;
    private Set<Long> roleIds;
}
