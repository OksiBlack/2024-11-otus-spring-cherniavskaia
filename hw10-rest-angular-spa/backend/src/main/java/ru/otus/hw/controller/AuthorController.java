package ru.otus.hw.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
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
import ru.otus.hw.service.AuthorService;

import java.util.List;

@Controller
@RequestMapping("/authors")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping(value = {"/list", "", "/"})
    public String listAuthors(Model theModel) {
        // get author from db
        List<AuthorDto> theAuthors = authorService.findAll();
        // add to the spring model
        theModel.addAttribute("authors", theAuthors);
        return "list-authors";
    }

    @GetMapping("/{authorId}")
    public String getById(@PathVariable Long authorId, Model theModel) {
        // get author from db
        AuthorDto author = authorService.findById(authorId);
        // add to the spring model
        theModel.addAttribute("author", author);
        return "single-author";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        // create model attribute to bind form data
        AuthorDto theAuthor = new AuthorDto();
        theModel.addAttribute("author", theAuthor);
        return "author-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("authorId") Long theId, Model theModel) {
        //get the author from the service
        AuthorDto theAuthor = authorService.findById(theId);
        //set author as a model attribute to pre-populate the form
        theModel.addAttribute("author", theAuthor);
        return "author-form";
    }

    @PostMapping("/save")
    public String saveAuthor(@ModelAttribute("author") @Valid AuthorDto theAuthor, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "author-form";
        }
        // save the author
        authorService.save(theAuthor);
        // use a redirect to prevent duplicate submissions
        return "redirect:/authors/list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("authorId") Long theId) {
        // delete the author
        authorService.deleteById(theId);
        // redirect to /author/list
        return "redirect:/authors/list";
    }
}
