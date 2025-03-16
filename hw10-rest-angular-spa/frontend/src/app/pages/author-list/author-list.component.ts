import {Component, OnInit} from '@angular/core';
import {AuthorService} from "../../services/author.service";
import {Router} from "@angular/router";
import {Author} from "../../model/author";
import {MatButton} from "@angular/material/button";
import {
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderCellDef,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    MatTable
} from "@angular/material/table";
import {MatList, MatListItem} from "@angular/material/list";

@Component({
    selector: 'app-author-list',
    imports: [
        MatButton,
        MatCell,
        MatCellDef,
        MatColumnDef,
        MatHeaderCell,
        MatHeaderRow,
        MatHeaderRowDef,
        MatList,
        MatListItem,
        MatRow,
        MatRowDef,
        MatTable,
        MatHeaderCellDef
    ],
    templateUrl: './author-list.component.html',
    styleUrl: './author-list.component.scss'
})
export class AuthorListComponent implements OnInit {
    authors: Author[] = [];
    displayedColumns: string[] = ['id', 'firstName', 'middleName', 'lastName', 'description', 'actions'];

    constructor(
        private readonly authService: AuthorService,
        private readonly router: Router,
    ) {
    }

    ngOnInit(): void {
        this.refreshAuthors();
    }

    private refreshAuthors(): void {
        this.authService.getAuthorList().subscribe(data => {
            this.authors = data;
        });
    }

    public goToAddAuthor() {
        this.router.navigate(['author-edit']);
    }

    public goToEditAuthor(author: Author) {
        if (typeof author === undefined || author.id === undefined) {
            return;
        }
        this.router.navigate(['author-edit', author.id]);
    }


    public deleteAuthor(author: Author) {
        if (typeof author === undefined || author.id === undefined) {
            return;
        }
        this.authService.deleteAuthor(author.id).subscribe(() => {
            this.refreshAuthors();
        });
    }

    public goToAuthorDetails(author: Author) {
        if (typeof author === undefined || author.id === undefined) {
            return;
        }
        this.router.navigate(['authors', author.id]);
    }
}
