package ru.otus.hw.api.rest.controller.management.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class AuthorityDto {
    private UUID id;

    @NotEmpty
    private String name;

    private String description;
}
