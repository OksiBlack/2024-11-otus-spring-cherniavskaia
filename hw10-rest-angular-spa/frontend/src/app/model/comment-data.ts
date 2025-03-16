import {Comment} from "./comment";

export class CommentData {
    id?: number;
    text?: string;
    bookId?: number;
    created?: Date;
    author?: string

    constructor(init?: Partial<Comment>) {
        Object.assign(this, init);
    }
}
