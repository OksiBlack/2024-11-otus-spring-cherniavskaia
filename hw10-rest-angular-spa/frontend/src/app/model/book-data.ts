import {Book} from "./book";

export class BookData {
    id?: number | undefined;
    title?: string | null;
    authorId?: number | undefined;
    genreIds?: (number | undefined)[] | undefined;
    description?: string | null;
    isbn?: string | null;
    serialNumber?: string | null;

    constructor(book: Book) {
        this.id = book.id;
        this.title = book.title;
        this.authorId = book.author?.id;
        this.genreIds = book.genres?.map(genre => genre.id);
        this.isbn = book.isbn;
        this.description = book.description;
        this.serialNumber = book.serialNumber
    }
}
