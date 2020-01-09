import {BookByPublisher} from './book-by-publisher';

export class BookByAuthor {
  bookId: string;
  title: string;
  authors: string[];

  constructor(book: BookByPublisher) {
    this.bookId = book.bookId;
    this.title = book.title;
    this.authors = book.authors;
  }
}
