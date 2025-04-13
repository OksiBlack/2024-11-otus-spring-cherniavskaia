package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookSearchFilter;
import ru.otus.hw.dto.request.SaveBookRequest;
import ru.otus.hw.mapper.BookMapper;
import ru.otus.hw.model.Book;

import java.util.HashSet;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AclBasedAuthorService aclBasedAuthorService;

    private final AclBasedGenreService aclBasedGenreService;

    private final BookMapper bookMapper;

    private final AclBasedBookService aclBasedBookService;


    @Transactional(readOnly = true)
    @Override
    public BookDto findById(Long id) {
        Book byId = aclBasedBookService.findById(id);
        return bookMapper.mapToDto(byId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll() {
        List<Book> all = aclBasedBookService.findAll();
        return all
            .stream()
            .map(bookMapper::mapToDto)
            .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDto> findAll(String keyword) {

        return aclBasedBookService.findAll(keyword).stream()
            .map(bookMapper::mapToDto).toList();
    }

    @Transactional
    @Override
    public List<BookDto> findAll(BookSearchFilter bookSearchFilter) {

        return aclBasedBookService.findAll(bookSearchFilter).stream()
            .map(bookMapper::mapToDto).toList();
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        aclBasedBookService.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return aclBasedBookService.existsById(id);
    }

    @Transactional
    @Override
    public BookDto save(SaveBookRequest saveBookRequest) {
        Long bookId = saveBookRequest.getId();
        Book book;
        if (bookId != null) {
            book = aclBasedBookService.findById(bookId);
        } else {
            book = new Book();
        }

        var author = aclBasedAuthorService.findById(saveBookRequest.getAuthorId());
        book.setAuthor(author);
        book.setTitle(saveBookRequest.getTitle());
        book.setDescription(saveBookRequest.getDescription());
        book.setIsbn(saveBookRequest.getIsbn());
        book.setSerialNumber(saveBookRequest.getSerialNumber());

        var genres = aclBasedGenreService.findAllByIds(new HashSet<>(saveBookRequest.getGenreIds()));

        book.setGenres(genres);
        return bookMapper.mapToDto(aclBasedBookService.save(book));
    }
}
