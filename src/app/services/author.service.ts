import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {environment} from '../../environments/environment';
import {catchError, Observable, throwError} from 'rxjs';
import {Author} from '../model/author';

@Injectable({
    providedIn: 'root'
})
export class AuthorService {

    constructor(private http: HttpClient) {
    }

    get contextPath(): string {
        return environment.apiUrl.concat('authors');
    }

    private getApiWithId(id: number): string {
        let idStr = (id === null || id === undefined) ? '' : ("" + id);
        return this.contextPath.concat('/', idStr);
    }


    public getAuthorById(id: number): Observable<Author> {
        return this.http.get<Author>(this.getApiWithId(id)).pipe(
            catchError(this.handleError)
        );
    }

    public getAuthorList(): Observable<Author[]> {
        return this.http.get<Author[]>(this.contextPath).pipe(
            catchError(this.handleError)
        );
    }

    private handleError(error: HttpErrorResponse) {
        let errorMsg = error.headers.get('errorMsgs')?.toString();
        errorMsg = errorMsg === undefined ? '' : errorMsg;
        console.log(errorMsg);
        return throwError(() => new Error(errorMsg));
    }

    public saveAuthor(author: Author): Observable<Author> {
        if (author.id !== undefined && author.id !== null) {
            return this.http.put<Author>(this.getApiWithId(author.id), author).pipe(
                catchError(this.handleError)
            );
        } else {
            return this.http.post<Author>(this.contextPath, author).pipe(
                catchError(this.handleError)
            );
        }
    }

    public deleteAuthor(id: number) {
        return this.http.delete(this.getApiWithId(id)).pipe(
            catchError(this.handleError)
        );
    }
}
