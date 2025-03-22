import {Routes} from '@angular/router';
import {BookListComponent} from './pages/book-list/book-list.component';
import {BookEditComponent} from './pages/book-edit/book-edit.component';
import {CommentEditComponent} from "./pages/comment-edit/comment-edit.component";
import {CommentListComponent} from "./pages/comment-list/comment-list.component";
import {SingleAuthorComponent} from "./pages/single-author/single-author.component";
import {SingleGenreComponent} from "./pages/single-genre/single-genre.component";
import {AuthorListComponent} from "./pages/author-list/author-list.component";
import {GenreListComponent} from "./pages/genre-list/genre-list.component";
import {HomeComponent} from "./pages/home/home.component";
import {AuthorEditComponent} from "./pages/author-edit/author-edit.component";
import {GenreEditComponent} from "./pages/genre-edit/genre-edit.component";
import {SingleBookComponent} from "./pages/single-book/single-book.component";

export const routes: Routes = [

  {path: '', component: HomeComponent},
  {path: 'books', component: BookListComponent},
  {path: 'authors', component: AuthorListComponent},
  {path: 'genres', component: GenreListComponent},
  {path: 'book-edit/:id', component: BookEditComponent},
  {path: 'book-edit', component: BookEditComponent},
  {path: 'author-edit/:id', component: AuthorEditComponent},
  {path: 'author-edit', component: AuthorEditComponent},
  {path: 'genre-edit', component: GenreEditComponent},
  {path: 'genre-edit/:id', component: GenreEditComponent},
  {path: 'books/:id/comments', component: CommentListComponent},
  {path: 'comment-edit', component: CommentEditComponent},
  {path: 'comment-edit/:id', component: CommentEditComponent},
  {path: 'authors/:id', component: SingleAuthorComponent},
  {path: 'genres/:id', component: SingleGenreComponent},
  {path: 'books/:id', component: SingleBookComponent},
];
