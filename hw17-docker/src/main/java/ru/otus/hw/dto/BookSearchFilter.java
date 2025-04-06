package ru.otus.hw.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookSearchFilter {
    private Long id;

    private String title;

    private String isbn;

    private String serialNumber;

    private String description;

    private String authorName;

    private String genreName;
}
