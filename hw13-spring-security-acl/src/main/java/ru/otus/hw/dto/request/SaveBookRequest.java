package ru.otus.hw.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@With
public class SaveBookRequest {
    @JsonIgnore
    private Long id;

    @NotNull
    @Size(min = 3, max = 300)
    private String title;

    @NotNull
    private Long authorId;

    private String isbn;

    private String serialNumber;

    @NotBlank
    @Size(min = 2, max = 1000)
    private String description;

    @Size(min = 1)
    @NotEmpty
    @Builder.Default
    private Set<Long> genreIds = new HashSet<>();
}