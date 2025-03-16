import {Comment} from "./comment";

export class CommentData {
    id?: number;
    text?: string;
    bookId?: number;
    created?: Date;
    author?: string

    // constructor(comment: Comment) {
    //     this.id = comment.id;
    //     this.text = comment.text;
    //     this.bookId = comment.book?.id;
    //     this.created = comment.created;
    //     this.author = comment.author
    // }

    constructor(init?: Partial<Comment>) {
        Object.assign(this, init);
    }
}
