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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookSaveRequest {
    private Long id;
    private String title;
    private Long authorId;

    private String isbn;
    private String serialNumber;
    private String description;

    private List<Long> genreIds = new ArrayList<>();
}