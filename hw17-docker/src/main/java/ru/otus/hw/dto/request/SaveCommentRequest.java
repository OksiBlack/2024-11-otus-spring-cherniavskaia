package ru.otus.hw.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveCommentRequest {

    @NotNull
    @Size(min = 10, max = 1000)
    private String text;

    private LocalDate created;

    @NotNull
    @Size(min = 3, max = 100)
    private String author;

    @NotNull
    private Long bookId;
}