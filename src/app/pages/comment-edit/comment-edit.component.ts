import {Component, OnInit} from '@angular/core';
import {MatFormField, MatFormFieldModule, MatLabel} from "@angular/material/form-field";
import {MatInput, MatInputModule} from "@angular/material/input";
import {FormsModule} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import {Comment} from "../../model/comment";
import {Book} from "../../model/book";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {BookService} from "../../services/book.service";
import {CommentService} from "../../services/comment.service";
import {CommentData} from '../../model/comment-data';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";

@Component({
    selector: 'app-comment-edit',
    standalone: true,
    imports: [MatFormField, MatInputModule, MatLabel, MatButtonModule,
        FormsModule, MatInput, MatFormFieldModule, MatDatepickerToggle, MatDatepicker, MatDatepickerInput],
    templateUrl: './comment-edit.component.html',
    styleUrl: './comment-edit.component.scss'
})
export class CommentEditComponent implements OnInit {
    comment: CommentData;
    books: Book[];


    constructor(private route: ActivatedRoute,
                private router: Router,
                private bookService: BookService,
                private commentService: CommentService) {
        this.comment = new Comment();
        this.books = [];
    }

    public saveComment() {
        if (this.comment.id === undefined) {
            this.commentService.createComment(this.comment).subscribe(() => this.goToBookComments(this.comment));
        } else {
            this.commentService.updateComment(this.comment.id, this.comment).subscribe(() => this.goToBookComments(this.comment));
        }
    }

    public goToBookList() {
        this.router.navigate(['books'])
    }

    private getCommentById(id: number) {
        if (id === null || id === undefined) {
            this.comment = new Comment();
            this.comment.text = "";
            return;
        }

        this.commentService.getCommentById(id).subscribe(data => {
            this.comment = data;

            if (typeof this.comment.bookId !== undefined) {
                this.books.forEach(element => {
                    if (element !== undefined && this.comment.bookId === element.id) {
                        this.comment.bookId = element.id;
                    }
                });
            }
        });
    }

    public ngOnInit(): void {
        this.bookService.getAllBooks().subscribe(data => {
            this.books = data;
        });

        this.route.params.subscribe((params: Params) => {
            let id = params['id'];
            this.getCommentById(id);
            const bookId = params['bookId']; // this is now the book ID passed
            if (bookId) {
                this.comment.bookId = bookId; // Assign the book ID from the route to the comment
            }
        });
    }

    public goToBookDetails(comment: CommentData) {
        this.router.navigate(['books', comment.bookId])
    }

    public goToBookComments(comment: CommentData) {
        this.router.navigate(['books', comment.bookId, 'comments'])
    }
}
