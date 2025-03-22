import {Component, OnInit} from '@angular/core';
import {Author} from "../../model/author";
import {FormsModule} from "@angular/forms";
import {MatButton} from "@angular/material/button";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {ActivatedRoute, Params, Router} from "@angular/router";
import {AuthorService} from "../../services/author.service";

@Component({
    selector: 'app-author-edit',
    imports: [
        FormsModule,
        MatButton,
        MatFormField,
        MatInput,
        MatLabel
    ],
    templateUrl: './author-edit.component.html',
    styleUrl: './author-edit.component.scss'
})
export class AuthorEditComponent implements OnInit {
    author: Author = new Author();

    constructor(private route: ActivatedRoute,
                private router: Router,
                private authorService: AuthorService) {
        this.author = new Author()
    }

    private getAuthorById(id: number) {
        if (id === null || id === undefined) {
            this.author = new Author();
            return;
        }
        this.authorService.getAuthorById(id).subscribe(data => {
            this.author = data;
            console.info("Author details: ", data)
        });
    }



    public saveAuthor(author: Author) {
        this.authorService.saveAuthor(author)
        .subscribe(() => this.goToAuthorList())
    }

    public goToAuthorList() {
        this.router.navigate(['authors'])
    }

    public ngOnInit(): void {
        this.route.params.subscribe((params: Params) => {
            let id = params['id'];
            this.getAuthorById(id);
        });
    }

}
