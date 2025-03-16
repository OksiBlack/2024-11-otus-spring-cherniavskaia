import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import {BookService} from "../../services/book.service";
import {AuthorService} from "../../services/author.service";
import {GenreService} from "../../services/genre.service";
import {Book} from "../../model/book";
import {MatAnchor, MatButton} from "@angular/material/button";
import {MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle, MatCardTitleGroup} from "@angular/material/card";
import {MatToolbar} from "@angular/material/toolbar";
import {NgForOf} from "@angular/common";
import {Genre} from "../../model/genre";
import {Author} from "../../model/author";

@Component({
    selector: 'app-single-book',
    imports: [
        MatButton,
        MatCard,
        MatCardContent,
        MatCardHeader,
        MatCardSubtitle,
        MatCardTitle,
        MatCardTitleGroup,
        MatToolbar,
        MatAnchor,
        NgForOf
    ],
    templateUrl: './single-book.component.html',
    styleUrl: './single-book.component.scss'
})
export class SingleBookComponent implements OnInit {
    book: Book;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private bookService: BookService,
                private genreService: GenreService,
                private authorService: AuthorService,
    ) {
        this.book = new Book()
    }

    public goToBookList() {
        this.router.navigate(['books'])
    }


    public goToEditBook(book: Book) {
        if (typeof book === undefined || book.id === undefined) {
            return;
        }
        this.router.navigate(['book-edit', book.id]);
    }

    private getBookById(id: number) {
        if (id === null || id === undefined) {
            this.book = new Book();
            return;
        }

        this.bookService.getBookById(id).subscribe(data => {
            this.book = data;
        });
    }

    public deleteBook(book: Book) {
        if (book === undefined || book.id === undefined) {
            return;
        }

        this.bookService.deleteBook(book.id).subscribe(() => {
            // Navigate only after the delete operation is successful
            this.router.navigate(['books']);
        });
    }

    public ngOnInit(): void {
        this.route.params.subscribe((params: Params) => {
            let id = params['id'];
            this.getBookById(id);
        });
    }

    public goToGenre(genre: Genre) {
        if (genre.id === null || genre.id === undefined) {
            return;
        }

        this.router.navigate(['genres', genre.id]);

    }

    public goToAuthorDetails(author: Author | undefined) {
        if (author === undefined || author.id === null || author.id === undefined) {
            return;
        }

        this.router.navigate(['authors', author.id]);

    }

    public showCommentsForBook(book: Book) {
        if (book === undefined || book.id === undefined) {
            return;
        }

        let finalUrl = this.router.getCurrentNavigation()?.finalUrl;
        let initialUrl = this.router.getCurrentNavigation()?.initialUrl;
        let extractedUrl = this.router.getCurrentNavigation()?.extractedUrl;

        console.info(finalUrl)
        console.info(initialUrl)
        console.info(extractedUrl)


        this.router.navigate(['books', book.id, 'comments']);

    }
}
