export class Author {
  id?: number;

  firstName?: string;
  middleName?: string; // Optional field
  lastName?: string;
  description?: string; // Optional field

  constructor(init?: Partial<Author>) {
    Object.assign(this, init);
  }

}
