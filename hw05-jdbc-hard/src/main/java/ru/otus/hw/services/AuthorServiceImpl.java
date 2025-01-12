package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author insert(String fullName) {
        Author author = new Author();
        author.setFullName(fullName);

        return authorRepository.insert(author);
    }

    @Override
    public Optional<Author> findById(long id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<Author> findAllByIds(Set<Long> ids) {
        return authorRepository.findAllByIds(ids);
    }

    @Override
    public void deleteById(long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public Author updateAuthor(long id, String fullName) {
        Author author = new Author();
        author.setFullName(fullName);
        author.setId(id);

        return authorRepository.update(author);
    }

    @Override
    public Author upsert(long id, String fullName) {
        if (existsById(id)) {
            return updateAuthor(id, fullName);
        } else {
            return insert(fullName);
        }
    }

    @Override
    public boolean existsById(long id) {
        return authorRepository.existsById(id);
    }

}
