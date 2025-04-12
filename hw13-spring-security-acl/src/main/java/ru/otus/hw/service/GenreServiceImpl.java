package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.mapper.GenreMapper;
import ru.otus.hw.model.Genre;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {

    private final AclBasedGenreService aclBasedGenreService;

    private final GenreMapper genreMapper;

    @Transactional(readOnly = true)
    @Override
    public List<GenreDto> findAll() {
        List<Genre> all = aclBasedGenreService.findAll();
        return all
            .stream()
            .map(genreMapper::mapToDto).toList();
    }

    @Transactional(readOnly = true)
    public List<GenreDto> findAllByIds(Set<Long> ids) {
        return aclBasedGenreService.findAllByIds(ids)
            .stream()
            .map(genreMapper::mapToDto).toList();
    }

    @Transactional
    @Override
    public GenreDto save(GenreDto genreDto) {
        Genre genre = genreMapper.mapToModel(genreDto);
        return genreMapper.mapToDto(aclBasedGenreService.save(genre));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        aclBasedGenreService.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public GenreDto findById(Long id) {
        return genreMapper.mapToDto(aclBasedGenreService.findById(id));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return aclBasedGenreService.existsById(id);
    }
}
