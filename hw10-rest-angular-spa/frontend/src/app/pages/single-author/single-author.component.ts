import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {MatFormFieldModule} from '@angular/material/form-field';
import {Author} from '../../model/author';
import {AuthorService} from '../../services/author.service';
import {MatButtonModule} from '@angular/material/button';
import {MatInputModule} from '@angular/material/input';
import {FormsModule} from '@angular/forms';
import {MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle, MatCardTitleGroup} from "@angular/material/card";
import {MatToolbar} from "@angular/material/toolbar";


@Component({
    templateUrl: './single-author.component.html',

    selector: 'app-single-author',
    imports: [MatInputModule, MatButtonModule,
        FormsModule, MatFormFieldModule, MatCardHeader, MatCard, MatCardTitle,
        MatCardSubtitle,
        MatCardTitleGroup, MatCardContent, MatToolbar],
    styleUrl: './single-author.component.scss'
})
export class SingleAuthorComponent implements OnInit {
    author: Author;

    constructor(private route: ActivatedRoute,
                private router: Router,
                private authorService: AuthorService
    ) {
        this.author = new Author()
    }

    goToAuthorList() {
        this.router.navigate(['authors'])
    }


    public goToEditAuthor(author: Author) {
        if (typeof author === undefined || author.id === undefined) {
            return;
        }
        this.router.navigate(['author-edit', author.id]);
    }

    private getAuthorById(id: number) {
        if (id === null || id === undefined) {
            this.author = new Author();
            return;
        }

        this.authorService.getAuthorById(id).subscribe(data => {
            this.author = data;
        });
    }

    public deleteAuthor(author: Author) {
        if (author === undefined || author.id === undefined) {
            return;
        }

        this.authorService.deleteAuthor(author.id).subscribe(() => {
            // Navigate only after the delete operation is successful
            this.router.navigate(['authors']);
        });
    }

    ngOnInit(): void {
        this.route.params.subscribe((params: Params) => {
            let id = params['id'];
            this.getAuthorById(id);
        });
    }
}
