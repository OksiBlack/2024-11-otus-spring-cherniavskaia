package ru.otus.hw.api.rest.controller.management.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class UserUpdateRequest {
    private String firstName;

    private String lastName;

    private String middleName;

    private LocalDate birthday;

    private Set<UUID> roleIds;
}
