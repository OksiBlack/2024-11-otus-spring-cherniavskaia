import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { Book } from '../model/book';
import { environment } from '../../environments/environment';
import { BookData } from '../model/book-data';
import {CommentService} from "./comment.service";

@Injectable({
  providedIn: 'root'
})
export class BookService {


  constructor(private readonly http: HttpClient, private commentService: CommentService) {}

  get contextPath(): string {
    return environment.apiUrl.concat('books');
  }

  private getApiWithId(id: number): string {
    let idStr = (id === null || id === undefined) ? '' : ("" + id);
    return this.contextPath.concat('/', idStr);
  }

  public getAllBooks(): Observable<Book[]> {
    return this.http.get<Book[]>(this.contextPath).pipe(
      catchError(this.handleError)
    );
  }

  public getBookById(id: number): Observable<Book> {
    return this.http.get<Book>(this.getApiWithId(id)).pipe(
      catchError(this.handleError)
    );
  }

  public saveBook(book: Book): Observable<Book> {
    if(book.id !== undefined && book.id !== null) {
      return this.http.put<Book>(this.getApiWithId(book.id), new BookData(book)).pipe(
        catchError(this.handleError)
      );
    } else {
      return this.http.post<Book>(this.contextPath, new BookData(book)).pipe(
        catchError(this.handleError)
      );
    }
  }

  public deleteBook(id: number) {
    return this.http.delete(this.getApiWithId(id)).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    console.log(error)
    let errorMsg = error.message
    return throwError(() => new Error(errorMsg));
  }
}
