package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

    private Long id;

    @Size(min = 1, max = 150)
    @NotBlank
    private String firstName;

    private String middleName;

    @NotBlank
    @Size(min = 1, max = 150)
    private String lastName;

    @Size(max = 1000)
    private String description;
}
