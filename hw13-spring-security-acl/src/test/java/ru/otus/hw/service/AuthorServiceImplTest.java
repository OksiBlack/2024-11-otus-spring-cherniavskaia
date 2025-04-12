package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.mapper.AuthorMapper;
import ru.otus.hw.model.Author;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthorServiceImplTest {

    @Mock
    private AclBasedAuthorService aclBasedAuthorService;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        Author author = new Author(); // Assuming Author has a default constructor
        AuthorDto authorDto = new AuthorDto(); // Assuming AuthorDto has a default constructor
        List<Author> authors = List.of(author);
        when(aclBasedAuthorService.findAll()).thenReturn(authors);
        when(authorMapper.mapToDto(author)).thenReturn(authorDto);

        List<AuthorDto> result = authorService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(authorDto, result.get(0));
        verify(aclBasedAuthorService).findAll();
        verify(authorMapper).mapToDto(author);
    }

    @Test
    public void testFindById() {
        Long authorId = 1L;
        Author author = new Author(); // Initialize the Author as per your needs
        AuthorDto authorDto = new AuthorDto(); // Initialize the AuthorDto as per your needs

        when(aclBasedAuthorService.findById(authorId)).thenReturn(author);
        when(authorMapper.mapToDto(author)).thenReturn(authorDto);

        AuthorDto result = authorService.findById(authorId);

        assertEquals(authorDto, result);
        verify(aclBasedAuthorService).findById(authorId);
        verify(authorMapper).mapToDto(author);
    }

    @Test
    public void testFindAllByIds() {
        Set<Long> authorIds = new HashSet<>(Set.of(1L, 2L));
        Author author1 = new Author(); 
        Author author2 = new Author(); 
        AuthorDto authorDto1 = new AuthorDto();
        AuthorDto authorDto2 = new AuthorDto();
        
        List<Author> authors = List.of(author1, author2);
        when(aclBasedAuthorService.findAllByIds(authorIds)).thenReturn(authors);
        when(authorMapper.mapToDto(author1)).thenReturn(authorDto1);
        when(authorMapper.mapToDto(author2)).thenReturn(authorDto2);

        List<AuthorDto> result = authorService.findAllByIds(authorIds);

        assertEquals(2, result.size());
        assertEquals(authorDto1, result.get(0));
        assertEquals(authorDto2, result.get(1));
        verify(aclBasedAuthorService).findAllByIds(authorIds);
        verify(authorMapper).mapToDto(author1);
        verify(authorMapper).mapToDto(author2);
    }

    @Test
    public void testDeleteById() {
        Long authorId = 1L;

        authorService.deleteById(authorId);

        verify(aclBasedAuthorService).deleteById(authorId);
    }

    @Test
    public void testExistsById() {
        Long authorId = 1L;
        when(aclBasedAuthorService.existsById(authorId)).thenReturn(true);

        boolean exists = authorService.existsById(authorId);

        assertTrue(exists);
        verify(aclBasedAuthorService).existsById(authorId);
    }

    @Test
    public void testSave() {
        AuthorDto authorDto = new AuthorDto(); // Create and fill the AuthorDto
        Author author = new Author(); // Create and fill the Author as needed
        
        when(authorMapper.mapToModel(authorDto)).thenReturn(author);
        when(aclBasedAuthorService.save(author)).thenReturn(author);
        when(authorMapper.mapToDto(author)).thenReturn(authorDto);

        AuthorDto result = authorService.save(authorDto);

        assertEquals(authorDto, result);
        verify(authorMapper).mapToModel(authorDto);
        verify(aclBasedAuthorService).save(author);
        verify(authorMapper).mapToDto(author);
    }
}
