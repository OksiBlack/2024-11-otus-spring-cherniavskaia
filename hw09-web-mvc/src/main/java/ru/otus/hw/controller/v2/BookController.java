package ru.otus.hw.controller.v2;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.controller.spec.builder.SpecBuilder;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.BookSearchFilter;
import ru.otus.hw.service.BookService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/books")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {

    final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/list")
    public String listBooks(Model theModel) {
        // get employees from db
        List<BookDto> theBooks = bookService.findAll();
        // add to the spring model
        theModel.addAttribute("books", theBooks);
        return "list-books";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        // create model attribute to bind form data
        BookDto theBook = new BookDto();
        theModel.addAttribute("books", theBook);
        return "book-form";
    }

    @GetMapping("/showFormForAdvancedSearch")
    public String showFormForSearch(Model theModel) {
        // create model attribute to bind form data
        BookSearchFilter bookFilter = new BookSearchFilter();
        theModel.addAttribute("bookFilter", bookFilter);

        List<BookDto> books = new ArrayList<>();
//        List<BookDto> books = bookService.findAll(SpecBuilder.Book.buildByFilter(bookFilter));
        theModel.addAttribute("books", books);

        return "book-search-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("bookId") Long theId, Model theModel) {
        //get the book from the service

        BookDto theBook = bookService.findById(theId).orElseThrow(() -> new EntityNotFoundException("Book not found [%s]"
            .formatted(theId)));
        //set book as a model attribute to pre-populate the form
        theModel.addAttribute("books", theBook);
        return "book-form";
    }

    @PostMapping("/save")
    public String saveBook(@ModelAttribute("books") BookDto theBook) {
        // save the book
        bookService.save(theBook);
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
}
