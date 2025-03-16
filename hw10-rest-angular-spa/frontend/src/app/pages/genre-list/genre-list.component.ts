import {Component, OnInit} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell, MatHeaderCellDef,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    MatTable
} from "@angular/material/table";
import {MatList, MatListItem} from "@angular/material/list";
import {Author} from "../../model/author";
import {AuthorService} from "../../services/author.service";
import {Router} from "@angular/router";
import {Genre} from "../../model/genre";
import {GenreService} from "../../services/genre.service";

@Component({
    selector: 'app-genre-list',
    imports: [
        MatButton,
        MatCell,
        MatCellDef,
        MatColumnDef,
        MatHeaderCell,
        MatHeaderRow,
        MatHeaderRowDef,
        MatList,
        MatListItem,
        MatRow,
        MatRowDef,
        MatTable,
        MatHeaderCellDef
    ],
    templateUrl: './genre-list.component.html',
    styleUrl: './genre-list.component.sass'
})
export class GenreListComponent implements OnInit {

    genres: Genre[] = [];
    displayedColumns: string[] = ['id', 'name', 'description', 'actions'];

    constructor(
        private readonly genreService: GenreService,
        private readonly router: Router,
    ) {
    }

    public ngOnInit(): void {
        this.refreshGenres();
    }

    private refreshGenres(): void {
        this.genreService.getGenreList().subscribe(data => {
            this.genres = data;
        });
    }

    public goToAddGenre() {
        this.router.navigate(['genre-edit']);
    }

    public goToEditGenre(genre: Genre) {
        if (typeof genre === undefined || genre.id === undefined) {
            return;
        }
        this.router.navigate(['genre-edit', genre.id]);
    }


    public deleteGenre(genre: Genre) {
        if (typeof genre === undefined || genre.id === undefined) {
            return;
        }
        this.genreService.deleteGenre(genre.id).subscribe(() => {
            this.refreshGenres();
        });
    }

    public goToGenreDetails(genre: Genre) {
        if (typeof genre === undefined || genre.id === undefined) {
            return;
        }
        this.router.navigate(['genres', genre.id]);
    }
}
