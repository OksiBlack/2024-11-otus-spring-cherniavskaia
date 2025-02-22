package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final Converter<Author, AuthorDto> authorConverter;

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll()
            .stream()
            .map(authorConverter::convert)
            .toList();
    }


    @Transactional
    @Override
    public AuthorDto create(String fullName) {
        Author author = new Author();
        author.setFullName(fullName);

        return authorConverter.convert(authorRepository.save(author));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<AuthorDto> findById(Long id) {
        return authorRepository.findById(id)
            .map(authorConverter::convert);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> findAllByIds(Set<Long> ids) {
        return authorRepository.findAllByIdIn(ids)
            .stream()
            .map(authorConverter::convert)
            .toList();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }

    @Transactional
    @Override
    public AuthorDto update(Long id, String fullName) {
        Author author = new Author();
        author.setFullName(fullName);
        author.setId(id);

        return authorConverter.convert(authorRepository.save(author));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return authorRepository.existsById(id);
    }
}
