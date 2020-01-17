import {BookByPublisher} from './book-by-publisher';

export class BookByAuthor {
  bookId: string;
  title: string;
  authors: string[];
  publisher: string;

  constructor(book: BookByPublisher) {
    this.bookId = book.bookId;
    this.title = book.title;
    this.authors = book.authors;
    this.publisher = book.publisherName;
  }
}
