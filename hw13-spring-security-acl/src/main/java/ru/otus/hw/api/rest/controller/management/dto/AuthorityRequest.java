package ru.otus.hw.api.rest.controller.management.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AuthorityRequest {
    @NotEmpty
    private String name;

    private String description;
}
