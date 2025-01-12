package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        return genreRepository.findAllByIds(ids);
    }

    @Override
    public Genre insert(String genreName) {
        Genre genre = new Genre();
        genre.setName(genreName);
        return genreRepository.insert(genre);
    }

    @Override
    public void deleteById(long id) {
        genreRepository.deleteById(id);
    }

    @Override
    public Optional<Genre> findById(long id) {
        return genreRepository.findById(id);
    }

    @Override
    public Genre updateGenre(long id, String name) {
        Genre genre = new Genre();
        genre.setName(name);
        genre.setId(id);

        return genreRepository.update(genre);
    }

    @Override
    public Genre upsertGenre(long id, String name) {
        if (existsById(id)) {
            return updateGenre(id, name);
        } else {
            return insert(name);
        }
    }

    @Override
    public boolean existsById(long id) {
        return genreRepository.existsById(id);
    }
}
