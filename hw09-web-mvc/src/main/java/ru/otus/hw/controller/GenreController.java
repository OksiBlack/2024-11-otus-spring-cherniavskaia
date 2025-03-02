package ru.otus.hw.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exception.EntityNotFoundException;
import ru.otus.hw.service.GenreService;

import java.util.List;

@Controller
@RequestMapping("/genres")
public class GenreController {
 private    final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") Long genreId, Model theModel) {
        GenreDto genreDto = genreService.findById(genreId).orElseThrow(
            () -> new EntityNotFoundException("Genre not found: [%s] ".formatted(genreId))
        );
        // add to the spring model
        theModel.addAttribute("genre", genreDto);
        return "single-genre";
    }

    @GetMapping(value = {"/list", "", "/"})
    public String listGenres(Model theModel) {

        // get genre from db
        List<GenreDto> theGenres = genreService.findAll();

        // add to the spring model
        theModel.addAttribute("genres", theGenres);

        return "list-genres";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {

        // create model attribute to bind form data
        GenreDto theGenre = new GenreDto();

        theModel.addAttribute("genre", theGenre);

        return "genre-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("genreId") Long theId, Model theModel) {
        //get the genre from the service
        GenreDto theGenre =
            genreService.findById(theId).orElseThrow(() -> new EntityNotFoundException("Genre not found: [%s]"
                .formatted(theId)));
        //set genre as a model attribute to pre-populate the form
        theModel.addAttribute("genre", theGenre);
        return "genre-form";
    }

    @PostMapping("/save")
    public String saveGenre(@ModelAttribute("genre") @Valid GenreDto genre, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "genre-form";
        }
        // save the genre
        genreService.save(genre);

        // use a redirect to prevent duplicate submissions
        return "redirect:/genres/list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("genreId") Long theId) {

        // delete the book
        genreService.deleteById(theId);

        // redirect to /genre/list
        return "redirect:/genres/list";

    }
}
