import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {Observable, catchError, throwError} from 'rxjs';
import {Genre} from '../model/genre';
import {Author} from "../model/author";
import {Book} from "../model/book";
import {BookData} from "../model/book-data";
import {Comment} from "../model/comment";

@Injectable({
    providedIn: 'root'
})
export class GenreService {

    constructor(private http: HttpClient) {
    }

    get contextPath(): string {
        return environment.apiUrl.concat('genres');
    }

    private getApiWithId(id: number): string {
        let idStr = (id === null || id === undefined) ? '' : ("" + id);
        return this.contextPath.concat('/', idStr);
    }

    public getGenreById(id: number): Observable<Genre> {
        return this.http.get<Genre>(this.getApiWithId(id)).pipe(
            catchError(this.handleError)
        );
    }

    public getGenreList(): Observable<Genre[]> {
        return this.http.get<Genre[]>(this.contextPath).pipe(
            catchError(this.handleError)
        );
    }

    private handleError(error: HttpErrorResponse) {
        console.log(error);
        let errorMsg = error.message
        return throwError(() => new Error(errorMsg));
    }


    public saveGenre(genre: Genre): Observable<Genre> {
        if (genre.id !== undefined && genre.id !== null) {
            return this.http.patch<Book>(this.getApiWithId(genre.id), genre).pipe(
                catchError(this.handleError)
            );
        } else {
            return this.http.post<Author>(this.contextPath, genre).pipe(
                catchError(this.handleError)
            );
        }
    }

    public deleteGenre(id: number) {
        return this.http.delete(this.getApiWithId(id)).pipe(
            catchError(this.handleError)
        );
    }
}
