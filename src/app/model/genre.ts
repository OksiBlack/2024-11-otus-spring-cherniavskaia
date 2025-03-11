export class Genre {
  id?: number;
  name?: string;

  description?: string; // Optional field

  constructor(init?: Partial<Genre>) {
    Object.assign(this, init);
  }
}
