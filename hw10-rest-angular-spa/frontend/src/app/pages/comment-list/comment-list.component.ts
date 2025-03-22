import {Component, OnInit} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {Book} from "../../model/book";
import {Comment} from "../../model/comment";
import {CommentService} from "../../services/comment.service";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {MatTableModule} from "@angular/material/table";
import {BookService} from "../../services/book.service";
import {MatList, MatListItem} from "@angular/material/list";
import {MatToolbar} from "@angular/material/toolbar";

@Component({
    selector: 'app-comment-list',
    standalone: true,
    imports: [MatTableModule, MatButton, MatList, MatListItem, MatToolbar],
    templateUrl: './comment-list.component.html',
    styleUrl: './comment-list.component.scss'
})
export class CommentListComponent implements OnInit {
    book: Book;
    comments: Comment[] = [];
    displayedColumns: string[] = ['id', 'text', 'author', 'created', 'actions'];

    constructor(private route: ActivatedRoute,
                private router: Router,
                private commentService: CommentService,
                private bookService: BookService) {
        this.book = new Book();
    }

    public goToEditComment(comment: Comment) {
        if (typeof comment === undefined || comment.id === undefined) {
            return;
        }
        this.router.navigate(['comment-edit', comment.id]);
    }

    public goToAddComment(book: Book) {
        this.router.navigate(['comment-edit', { bookId: book.id }]);
    }

    public goToBookList() {
        this.router.navigate(['books'])
    }

    public deleteComment(comment: Comment) {
        if (typeof comment === undefined || comment.id === undefined) {
            return;
        }
        this.commentService.deleteComment(comment.id).subscribe(() => {
            this.refreshComments();
        });
    }

    private refreshComments() {
        if (this.book !== undefined) {
            let id = this.book.id;
            if (id === undefined) {
                return;
            }
            this.commentService.getCommentsForBookId(id).subscribe(data => {
                this.comments = data;
            });
        }
    }

    public ngOnInit(): void {
        this.route.params.subscribe((params: Params) => {
            let id = params['id'];
            this.bookService.getBookById(id).subscribe(data => {
                this.book = data;
                this.refreshComments();
            });
        });
    }

    public goToBookDetails( book:Book) {
        this.router.navigate(['books', book.id])

    }
}
