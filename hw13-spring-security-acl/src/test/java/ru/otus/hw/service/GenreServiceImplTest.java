package ru.otus.hw.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.model.Genre;

class GenreServiceImplTest {

    private AclBasedGenreService aclBasedGenreService;
    private GenreMapper genreMapper;
    private GenreServiceImpl genreService;

    @BeforeEach
    void setUp() {
        aclBasedGenreService = Mockito.mock(AclBasedGenreService.class);
        genreMapper = Mockito.mock(GenreMapper.class);
        genreService = new GenreServiceImpl(aclBasedGenreService, genreMapper);
    }

    @Test
    void findAll_shouldReturnListOfGenreDto() {
        // Given
        List<Genre> genres = List.of(
             Genre.builder().id(1L).name("Fantasy").description("Fantasy Genre").build(),
             Genre.builder().id(2L).name("Science Fiction").description("Sci-Fi Genre").build()
        );

        when(aclBasedGenreService.findAll()).thenReturn(genres);
        when(genreMapper.mapToDto(any(Genre.class))).thenAnswer(invocation -> {
            Genre genre = invocation.getArgument(0);
            return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
        });

        // When
        List<GenreDto> result = genreService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(GenreDto::getName).containsExactly("Fantasy", "Science Fiction");
        verify(aclBasedGenreService, times(1)).findAll();
    }

    @Test
    void findAllByIds_shouldReturnListOfGenreDto() {
        // Given
        Set<Long> ids = new HashSet<>();
        ids.add(1L);
        Set<Genre> genres = Set.of(
            Genre.builder().id(1L).name("Fantasy").description("Fantasy Genre").build()
        );

        when(aclBasedGenreService.findAllByIds(ids)).thenReturn(genres);
        when(genreMapper.mapToDto(any(Genre.class))).thenAnswer(invocation -> {
            Genre genre = invocation.getArgument(0);
            return GenreDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
        });

        // When
        List<GenreDto> result = genreService.findAllByIds(ids);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).extracting(GenreDto::getName).containsExactly("Fantasy");
    }

    @Test
    void save_shouldReturnSavedGenreDto() {
        // Given
        GenreDto dto = GenreDto.builder().name("Horror").build();
        Genre genreToSave = Genre.builder().name("Horror").build();
        Genre savedGenre = Genre.builder().id(1L).name("Horror").build();

        when(genreMapper.mapToModel(dto)).thenReturn(genreToSave);
        when(aclBasedGenreService.save(genreToSave)).thenReturn(savedGenre);
        when(genreMapper.mapToDto(savedGenre)).thenReturn(dto);

        // When
        GenreDto result = genreService.save(dto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Horror");
        verify(aclBasedGenreService, times(1)).save(genreToSave);
    }

    @Test
    void deleteById_shouldCallDeleteOnAclBasedGenreService() {
        // Given
        Long idToDelete = 1L;

        // When
        genreService.deleteById(idToDelete);

        // Then
        verify(aclBasedGenreService, times(1)).deleteById(idToDelete);
    }

    @Test
    void findById_shouldReturnGenreDto() {
        // Given
        Long id = 1L;
        Genre genre =  Genre.builder().id(id).name("Thriller").description("Thriller Genre").build();
        GenreDto genreDto = GenreDto.builder().id(id).name("Thriller").build();

        when(aclBasedGenreService.findById(id)).thenReturn(genre);
        when(genreMapper.mapToDto(genre)).thenReturn(genreDto);

        // When
        GenreDto result = genreService.findById(id);

        // Then
        assertThat(result.getName()).isEqualTo("Thriller");
    }

    @Test
    void existsById_shouldReturnTrueIfExists() {
        // Given
        Long id = 1L;
        when(aclBasedGenreService.existsById(id)).thenReturn(true);

        // When
        boolean exists = genreService.existsById(id);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsById_shouldReturnFalseIfNotExists() {
        // Given
        Long id = 2L;
        when(aclBasedGenreService.existsById(id)).thenReturn(false);

        // When
        boolean exists = genreService.existsById(id);

        // Then
        assertThat(exists).isFalse();
    }
}
