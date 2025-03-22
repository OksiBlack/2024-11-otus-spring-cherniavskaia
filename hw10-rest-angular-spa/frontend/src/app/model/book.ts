import {Author} from "./author";
import {Genre} from "./genre";

export class Book {
  id?: number;
  title?: string;
  description?: string; // Optional field
  serialNumber?: string;
  isbn?: string;
  author?: Author; // This would be of Author type
  genres?: Genre[]; // Collection of genres, optional

  constructor(init?: Partial<Book>) {
    Object.assign(this, init);
  }
}