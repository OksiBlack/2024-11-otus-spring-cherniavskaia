import {Book} from "./book";

export class Comment {
  id?: number;
  book?: Book; // Association with Book
  text?: string;
  created?: Date; // Assuming you use Date to handle LocalDate
  author?: string;

  constructor(init?: Partial<Comment>) {
    Object.assign(this, init);
  }
}
