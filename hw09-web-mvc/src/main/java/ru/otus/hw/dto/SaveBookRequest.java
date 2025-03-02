package ru.otus.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveBookRequest {
    private Long id;

    @NotNull
    @Size(min = 3, max = 30)
    private String title;

    @NotNull
    private Long authorId;

    private String isbn;

    private String serialNumber;

    @NotBlank
    @Size(min = 2, max = 30)
    private String description;

    @Size(min = 1)
    @NotEmpty
    private Set<Long> genreIds = new HashSet<>();
}