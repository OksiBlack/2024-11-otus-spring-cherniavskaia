package ru.otus.hw.dto.request;

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
public class SaveGenreRequest {


    @Size(min = 3, max = 30)
    @NotBlank
    private String name;

    @Size(min = 3, max = 1000)
    @NotBlank
    private String description;
}
