package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateBookRequest {
    private String title;
    private String isbn;
    private String serialNumber;
    private String description;
    private Long authorId;

    private List<Long> genreIds = new ArrayList<>();
}