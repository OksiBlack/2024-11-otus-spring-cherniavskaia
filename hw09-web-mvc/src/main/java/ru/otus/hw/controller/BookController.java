package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.service.AuthorService;
import ru.otus.hw.service.BookService;
import ru.otus.hw.service.GenreService;

import java.util.List;

//@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/books")
    public String listBook(Model model) {
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "list";
    }

//    @PostMapping
//    public String createBook(Model model) {
//        List<AuthorDto> author = authorService.findAll();
//        List<GenreDto> genre = genreService.findAll();
//
//        GenreDto genreDtoOne = new GenreDto();
//        GenreDto genreDtoTwo = new GenreDto();
//
//        BookCreateDto book = new BookCreateDto();
//        book.setGenres(List.of(genreDtoOne, genreDtoTwo));
//        model.addAttribute("book", book);
//        model.addAttribute("authors", author);
//        model.addAttribute("genres", genre);
//        return "create";
//    }
//
//    @PostMapping("/books")
//    public String createBook(@Valid BookCreateDto book) {
////        bookService.create(book);
//        return "redirect:/";
//    }
//
//    @PostMapping("/book/edit")
//    public String editBook(@RequestParam("id") long id, Model model) {
//
//        BookDto book = bookService.findById(id).orElseThrow(
//                () -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
//        List<AuthorDto> author = authorService.findAll();
//        List<GenreDto> genre = genreService.findAll();
//
//        model.addAttribute("book", book);
//        model.addAttribute("authors", author);
//        model.addAttribute("genres", genre);
//        return "edit";
//    }
//
//    @PostMapping("/book/save")
//    public String saveBook(@Valid BookUpdateDto book) {
////        bookService.update(book);
//        return "redirect:/";
//    }
//
//    @PostMapping("/book/delete")
//    public String deleteBook(@RequestParam("id") long id) {
//        bookService.deleteById(id);
//        return "redirect:/";
//    }
}
