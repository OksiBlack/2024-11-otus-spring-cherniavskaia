import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {Observable, catchError, throwError} from 'rxjs';
import {Comment} from '../model/comment';
import {CommentData} from '../model/comment-data';

@Injectable({
    providedIn: 'root'
})
export class CommentService {
    private commentsApiPath: string = 'comments';
    private booksApiPath: string = 'books';

    // private endPoint: string = environment.apiUrl;

    constructor(private http: HttpClient) {
    }

    // get contextPath(): string {
    //     return this.endPoint.concat(this.commentsApiPath);
    // }

    public getCommentApiPathWithId(id: number): string {
        let idStr = (id === null || id === undefined) ? '' : ("" + id);
        return environment.apiUrl.concat(this.commentsApiPath, '/', idStr);
    }

    private getCommentApiPathForBookIdWithId(bookId: number|undefined): string {
        if (bookId === undefined) {
            return this.commentsApiPath
        }

        return environment.apiUrl.concat(this.booksApiPath, '/',
            ("" + bookId), '/', this.commentsApiPath);
    }

    public getCommentById(id: number): Observable<Comment> {
        return this.http.get<Comment>(this.getCommentApiPathWithId(id)).pipe(
            catchError(this.handleError)
        );
    }

    public getCommentsForBookId(id: number): Observable<Comment[]> {
        return this.http.get<Comment[]>(this.getCommentApiPathForBookIdWithId(id)).pipe(
            catchError(this.handleError)
        );
    }

    public updateComment(id: number, comment: CommentData): Observable<Comment> {
        return this.http.put<CommentData>(this.getCommentApiPathWithId(id), comment).pipe(
            catchError(this.handleError)
        );
    }

    public createComment(comment: CommentData): Observable<Comment> {

        return this.http.post<CommentData>(this.getCommentApiPathForBookIdWithId(comment.bookId), comment).pipe(
            catchError(this.handleError)
        );
    }

    public deleteComment(id: number) {
        return this.http.delete(this.getCommentApiPathWithId(id)).pipe(
            catchError(this.handleError)
        );
    }

    private handleError(error: HttpErrorResponse) {
        // let errorMsg = error.headers.get('errorMsgs')?.toString();
        let errorMsg = error.message;
        console.log(error);
        return throwError(() => new Error(errorMsg));
    }
}
