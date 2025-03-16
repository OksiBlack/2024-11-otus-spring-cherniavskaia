package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookSearchFilter;
import ru.otus.hw.dto.CommentDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.dto.request.SaveBookRequest;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.mapper.BookToSaveRequestConverter;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.CommentService;
import ru.otus.hw.service.GenreService;
import ru.otus.hw.service.spec.builder.SpecBuilder;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {

    private final BookService bookService;

    private final CommentService commentService;

    private final GenreService genreService;

    private final AuthorService authorService;

    private final BookToSaveRequestConverter bookToSaveRequestConverter;

    @GetMapping(value = {"/list", "", "/"})
    public String listBooks(Model theModel) {
        // get employees from db
        List<BookDto> theBooks = bookService.findAll();
        // add to the spring model
        theModel.addAttribute("books", theBooks);
        return "list-books";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long bookId, Model theModel) {
        BookDto bookDto = bookService.findById(bookId).orElseThrow(
            () -> new EntityNotFoundException("Book not found: [%s] ".formatted(bookId))
        );
        // add to the spring model
        theModel.addAttribute("book", bookDto);
        List<CommentDto> comments = commentService.findAllByBookId(bookId);
        theModel.addAttribute("comments", comments);

        return "single-book";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {

        addAuthorsAndGenresToModel(theModel);

        // create model attribute to bind form data
        SaveBookRequest bookRequest = new SaveBookRequest();
        theModel.addAttribute("book", bookRequest);
        return "book-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("bookId") Long bookId, Model theModel) {
        //get the book from the service
        addAuthorsAndGenresToModel(theModel);

        BookDto book = bookService.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));

        SaveBookRequest saveBookRequest = bookToSaveRequestConverter.convert(book);
        theModel.addAttribute("book", saveBookRequest);

        return "book-form";
    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute("book") @Valid SaveBookRequest saveBookRequest,
                           BindingResult bindingResult,
                           Model theModel
    ) {
        // save the book

        if (bindingResult.hasErrors()) {
            addAuthorsAndGenresToModel(theModel);
            return "book-form";
        }
        bookService.save(saveBookRequest);

        // use a redirect to prevent duplicate submissions
        return "redirect:/books/list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("bookId") Long theId) {
        // delete the book
        bookService.deleteById(theId);
        return "redirect:/books/list";
    }

    @PostMapping(value = "/search/advanced",
        consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
    )
    public String findBookByFilter(Model model, BookSearchFilter bookSearchFilter) {

        List<BookDto> filtered = bookService.findAll(SpecBuilder.Book.buildByFilter(bookSearchFilter));
        model.addAttribute("books", filtered);
        model.addAttribute("bookFilter", bookSearchFilter);

        log.info("{}", filtered);
        return "list-books";
    }

    @ModelAttribute
    public void addBookSearchFilterAttributePlaceHolderToRenderSearchForm(Model model) {
        BookSearchFilter bookFilter = new BookSearchFilter();
        model.addAttribute("bookFilter", bookFilter);
    }

    @PostMapping("/search")
    public String findBookByKeyword(Model model, @Param("keyword") String keyword) {

        List<BookDto> filtered = bookService.findAll(SpecBuilder.Book.buildByKeyword(keyword));
        model.addAttribute("books", filtered);
        model.addAttribute("filterKeyword", keyword);
        return "list-books";
    }

    private void addAuthorsAndGenresToModel(Model model) {
        List<AuthorDto> authors = authorService.findAll();
        model.addAttribute("authors", authors);

        List<GenreDto> genres = genreService.findAll();
        model.addAttribute("genres", genres);
    }
}
