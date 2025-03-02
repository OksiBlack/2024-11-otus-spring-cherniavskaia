package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.model.Genre;
import ru.otus.hw.repository.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    private final GenreMapper genreMapper;

    @Transactional(readOnly = true)
    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll()
            .stream()
            .map(genreMapper::mapToDto).toList();
    }

    @Transactional(readOnly = true)
    public List<GenreDto> findAllByIds(Set<Long> ids) {
        return genreRepository.findAllByIdIn(ids)
            .stream()
            .map(genreMapper::mapToDto).toList();
    }

    @Transactional
    @Override
    public GenreDto save(GenreDto genreDto) {
        Genre genre = genreMapper.mapToModel(genreDto);
        return genreMapper.mapToDto(genreRepository.save(genre));
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
            .map(genreMapper::mapToDto);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return genreRepository.existsById(id);
    }
}
