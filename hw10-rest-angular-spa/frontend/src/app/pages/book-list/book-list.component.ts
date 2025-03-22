import {Component, OnInit} from '@angular/core';
import {BookService} from '../../services/book.service';
import {Book} from '../../model/book';
import {MatTableModule} from '@angular/material/table';
import {Router} from '@angular/router';
import {MatAnchor, MatButton} from '@angular/material/button';
import {MatList, MatListItem} from "@angular/material/list";
import {Author} from "../../model/author";
import {AuthorService} from "../../services/author.service";
import {NgForOf} from "@angular/common";
import {Genre} from "../../model/genre";
import {CommentService} from "../../services/comment.service";

@Component({
    selector: 'app-book-list',
    standalone: true,
    imports: [MatTableModule, MatButton, MatList, MatListItem, MatAnchor, NgForOf],
    templateUrl: './book-list.component.html',
    styleUrl: './book-list.component.scss'
})
export class BookListComponent implements OnInit {
    books: Book[] = [];
    displayedColumns: string[] = ['id', 'title', 'author', 'genres', 'serialNumber', 'isbn','description', 'actions'];

    constructor(private bookService: BookService,
                private authorService: AuthorService,
                private commentService: CommentService,
                private router: Router,
    ) {
    }

    ngOnInit(): void {
        this.refreshBooks();
    }

    private refreshBooks(): void {
        this.bookService.getAllBooks().subscribe(data => {
            this.books = data;
        });
    }

    public goToAddBook() {
        this.router.navigate(['book-edit']);
    }

    public goToEditBook(book: Book) {
        if (typeof book === undefined || book.id === undefined) {
            return;
        }
        this.router.navigate(['book-edit', book.id]);
    }

    public showComments(book: Book) {
        if (typeof book === undefined || book.id === undefined) {
            return;
        }
        this.router.navigate(['books', book.id, 'comments']);
    }

    public deleteBook(book: Book) {
        if (typeof book === undefined || book.id === undefined) {
            return;
        }
        this.bookService.deleteBook(book.id).subscribe(() => {
            this.refreshBooks();
        });
    }

    public goToAuthorDetails(author: Author) {
        if (typeof author === undefined || author.id === undefined) {
            return;
        }
        this.router.navigate(['authors/', author.id]);
    }

    public goToGenreDetails(genre: Genre) {
        if (typeof genre === undefined || genre.id === undefined) {
            return;
        }
        this.router.navigate(['genres', genre.id]);
    }

    public goToBookDetails(book: Book) {
        if (typeof book === undefined || book.id === undefined) {
            return;
        }
        this.router.navigate(['books', book.id]);
    }
}
