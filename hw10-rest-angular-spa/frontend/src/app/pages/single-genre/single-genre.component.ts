import {Component, OnInit} from '@angular/core';
import {Genre} from "../../model/genre";
import {MatButton} from "@angular/material/button";
import {MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle, MatCardTitleGroup} from "@angular/material/card";
import {MatToolbar} from "@angular/material/toolbar";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {GenreService} from "../../services/genre.service";

@Component({
  selector: 'app-single-genre',
    imports: [
        MatButton,
        MatCard,
        MatCardContent,
        MatCardHeader,
        MatCardSubtitle,
        MatCardTitle,
        MatCardTitleGroup,
        MatToolbar
    ],
  templateUrl: './single-genre.component.html',
  styleUrl: './single-genre.component.scss'
})
export class SingleGenreComponent implements OnInit {

    genre: Genre;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private genreService: GenreService
    ) {
        this.genre = new Genre()
    }

    goToGenreList() {
        this.router.navigate(['genres'])
    }


    public goToEditGenre(genre: Genre) {
        if (typeof genre === undefined || genre.id === undefined) {
            return;
        }
        this.router.navigate(['genre-edit', genre.id]);
    }

    private getGenreById(id: number) {
        if (id === null || id === undefined) {
            this.genre = new Genre();
            return;
        }

        this.genreService.getGenreById(id).subscribe(data => {
            this.genre = data;
        });
    }

    public deleteGenre(genre: Genre) {
        if (genre === undefined || genre.id === undefined) {
            return;
        }

        this.genreService.deleteGenre(genre.id).subscribe(() => {
            // Navigate only after the delete operation is successful
            this.router.navigate(['genres']);
        });
    }

    ngOnInit(): void {
        this.route.params.subscribe((params: Params) => {
            let id = params['id'];
            this.getGenreById(id);
        });
    }
}
