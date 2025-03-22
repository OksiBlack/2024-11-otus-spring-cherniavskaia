import {Book} from "./book";

export class Comment {
    id?: number;
    book?: Book;
    text?: string;
    created?: Date;
    author?: string;

    constructor(init?: Partial<Comment>) {
        Object.assign(this, init);
    }
}
