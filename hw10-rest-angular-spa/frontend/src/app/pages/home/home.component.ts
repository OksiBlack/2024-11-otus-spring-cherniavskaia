import {Component} from '@angular/core';
import {MatCard, MatCardActions, MatCardContent, MatCardImage} from "@angular/material/card";
import {NgForOf} from "@angular/common";
import {RouterLink} from "@angular/router";
import {MatButton} from "@angular/material/button";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";

@Component({
  selector: 'app-home',
  imports: [
    MatCardContent,
    MatCard,
    NgForOf,
    RouterLink,
    MatCardActions,
    MatButton,
    MatGridList,
    MatGridTile,
    MatCardImage
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  cards = [
    {title: 'BOOKS', image: 'assets/images/books.jpg', link: '/books', buttonText: 'BOOKS'},
    {title: 'AUTHORS', image: 'assets/images/authors.jpg', link: '/authors', buttonText: 'AUTHORS'},
    {title: 'GENRES', image: 'assets/images/genres.jpeg', link: '/genres', buttonText: 'GENRES'}
  ];
}