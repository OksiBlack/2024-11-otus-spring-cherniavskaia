import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {MatFormField, MatFormFieldModule, MatLabel} from '@angular/material/form-field';
import {MatOption, MatSelect, MatSelectModule} from '@angular/material/select';
import {NgFor} from '@angular/common';
import {Book} from '../../model/book';
import {Author} from '../../model/author';
import {Genre} from '../../model/genre';
import {BookService} from '../../services/book.service';
import {AuthorService} from '../../services/author.service';
import {GenreService} from '../../services/genre.service';
import {MatButtonModule} from '@angular/material/button';
import {MatInput, MatInputModule} from '@angular/material/input';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

@Component({
    selector: 'app-book-edit',
    standalone: true,
    imports: [MatFormFieldModule, MatSelectModule, FormsModule, ReactiveFormsModule,
        MatFormField, MatInputModule, MatLabel, MatSelect, MatOption, MatButtonModule,
        NgFor, FormsModule, MatInput, MatFormFieldModule],
    templateUrl: './book-edit.component.html',
    styleUrl: './book-edit.component.scss'
})
export class BookEditComponent implements OnInit {
    book: Book;
    authors: Author[];
    genres: Genre[];

    authorsMap: Map<number | undefined, Author>;
    genresMap: Map<number | undefined, Genre>;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private bookService: BookService,
                private authorService: AuthorService,
                private genreService: GenreService) {
        this.book = new Book()
        this.authors = [];
        this.genres = [];
        this.authorsMap = new Map<number, Author>();
        this.genresMap = new Map<number, Genre>();
    }

    saveBook() {
        this.bookService.saveBook(this.book).subscribe(() => this.goToBookList());
    }

    goToBookList() {
        this.router.navigate(['books'])
    }

    private getBookById(id: number) {
        if (id === null || id === undefined) {
            this.book = new Book();
            this.book.title = "";
            this.book.genres = [];
            return;
        }
        this.bookService.getBookById(id).subscribe(data => {
            this.book = data;
            console.info("Book details: ", data)

            // Check if author is present and set it from the authors list
            if (typeof this.book.author !== undefined &&
                this.book.author?.id !== undefined) {
                this.book.author = this.authorsMap.get(this.book.author.id);
            }

            const genreList: Genre[] = [];
            this.book.genres?.forEach(genre => {
                let items = this.genresMap.get(genre.id);
                if (items != undefined) {
                    genreList.push(items)
                }
            });

            this.book.genres = genreList;
        });
    }

    public populateAuthorsMap(authors: Author[]) {
        authors.forEach((author: Author) => {
            if (author.id != null) {
                this.authorsMap.set(author.id, author);
            }
        });

        console.info("Authors map:", this.authorsMap)

    }

    public populateGenresMap(genres: Genre[]) {
        genres.forEach((genre: Genre) => {
            if (genre.id != null) {
                console.info(genre)
                this.genresMap.set(genre.id, genre);
            }
        });
        console.info("Genres map:", this.genresMap)
    }


    ngOnInit(): void {
        this.authorService.getAuthorList().subscribe(data => {
            this.authors = data;
            this.populateAuthorsMap(data)
        });

        this.genreService.getGenreList().subscribe(data => {
            this.genres = data;
            this.populateGenresMap(this.genres);
        });

        this.route.params.subscribe((params: Params) => {
            let id = params['id'];
            this.getBookById(id);
        });
    }
}
