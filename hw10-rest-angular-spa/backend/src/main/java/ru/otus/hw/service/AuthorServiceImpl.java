package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.model.Author;
import ru.otus.hw.repository.AuthorRepository;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll()
            .stream()
            .map(authorMapper::mapToDto)
            .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public AuthorDto findById(Long id) {
        return authorRepository.findById(id)
            .map(authorMapper::mapToDto)
            .orElseThrow(() -> new EntityNotFoundException("Author with id [%s] not found".formatted(id)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> findAllByIds(Set<Long> ids) {
        return authorRepository.findAllByIdIn(ids)
            .stream()
            .map(authorMapper::mapToDto)
            .toList();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return authorRepository.existsById(id);
    }

    @Transactional
    @Override
    public AuthorDto save(AuthorDto theAuthor) {
        Author author = authorMapper.mapToModel(theAuthor);
        Author saved = authorRepository.save(author);
        return authorMapper.mapToDto(saved);
    }
}
