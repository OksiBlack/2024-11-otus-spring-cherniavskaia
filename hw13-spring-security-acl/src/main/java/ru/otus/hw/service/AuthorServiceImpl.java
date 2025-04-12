package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.model.Author;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AclBasedAuthorService aclBasedAuthorService;

    private final AuthorMapper authorMapper;

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> findAll() {
        return aclBasedAuthorService.findAll()
            .stream()
            .map(authorMapper::mapToDto)
            .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public AuthorDto findById(Long id) {
        return authorMapper.mapToDto(aclBasedAuthorService.findById(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<AuthorDto> findAllByIds(Set<Long> ids) {
        return aclBasedAuthorService.findAllByIds(ids)
            .stream()
            .map(authorMapper::mapToDto)
            .toList();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        aclBasedAuthorService.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return aclBasedAuthorService.existsById(id);
    }

    @Transactional
    @Override
    public AuthorDto save(AuthorDto theAuthor) {
        Author author = authorMapper.mapToModel(theAuthor);
        return authorMapper.mapToDto(aclBasedAuthorService.save(author));
    }
}
