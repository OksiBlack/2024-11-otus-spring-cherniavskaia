package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final Converter<Genre, GenreDto> genreConverter;

    @Transactional(readOnly = true)
    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll()
            .stream()
            .map(genreConverter::convert).toList();
    }

    @Transactional(readOnly = true)
    public List<GenreDto> findAllByIds(Set<Long> ids) {
        return genreRepository.findAllByIdIn(ids)
            .stream()
            .map(genreConverter::convert).toList();
    }

    @Transactional
    @Override
    public GenreDto create(String genreName) {
        Genre genre = new Genre();
        genre.setName(genreName);
        return genreConverter.convert(genreRepository.save(genre));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        genreRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<GenreDto> findById(Long id) {
        return genreRepository.findById(id)
            .map(genreConverter::convert);
    }

    @Transactional
    @Override
    public GenreDto update(Long id, String name) {
        Genre genre = new Genre();
        genre.setName(name);
        genre.setId(id);

        return genreConverter.convert(genreRepository.save(genre));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return genreRepository.existsById(id);
    }
}
