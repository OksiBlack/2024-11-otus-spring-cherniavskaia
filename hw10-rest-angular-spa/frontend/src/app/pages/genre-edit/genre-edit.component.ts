import {Component, OnInit} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {MatButton} from "@angular/material/button";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {Genre} from "../../model/genre";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {GenreService} from "../../services/genre.service";
import {MatInputModule} from '@angular/material/input';

@Component({
    selector: 'app-genre-edit',
    imports: [
        FormsModule,
        MatButton,
        MatFormField,
        MatLabel,
        MatInputModule
    ],
    templateUrl: './genre-edit.component.html',
    styleUrl: './genre-edit.component.scss'
})
export class GenreEditComponent implements OnInit {
    genre: Genre;

    constructor(
        private genreService: GenreService,
        private route: ActivatedRoute,
        private router: Router,
    ) {
        this.genre = new Genre()
    }


    private getGenreById(id: number) {
        if (id === null || id === undefined) {
            this.genre = new Genre();
            return;
        }
        this.genreService.getGenreById(id).subscribe(data => {
            this.genre = data;
            console.info("Genre details: ", data)
        });
    }


    goToGenreList() {
        this.router.navigate(['genres'])
    }


    saveGenre() {
        this.genreService.saveGenre(this.genre).subscribe(() => this.goToGenreList());
    }


    ngOnInit(): void {

        this.route.params.subscribe((params: Params) => {
            let id = params['id'];
            this.getGenreById(id);
        });
    }

}
