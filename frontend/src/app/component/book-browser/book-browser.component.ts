import {Component, ElementRef, NgZone, OnInit, ViewChild} from '@angular/core';
import {BookService} from '../../service/book.service';
import {BookByAuthor} from '../../model/book-by-author';
import {MatTable, MatTableDataSource} from '@angular/material';
import {Router} from '@angular/router';
import {error} from 'util';
import {HttpErrorResponse} from '@angular/common/http';
import {throwError} from 'rxjs';

@Component({
  selector: 'app-book-browser',
  templateUrl: './book-browser.component.html',
  styleUrls: ['./book-browser.component.css']
})
export class BookBrowserComponent implements OnInit {

  @ViewChild('table', {static: false}) table: MatTable<BookByAuthor>;

  private error = false;
  private errorMessage: string;
  private result: BookByAuthor[] = [];
  private displayedColumns = ['index', 'title', 'authors'];

  constructor(private bookService: BookService, private router: Router) {
  }

  public showResults(title, author, publisher): void {
    if (title) {
      title = title.value;
    }
    if (author) {
      author = author.value;
    }
    if (publisher) {
      publisher = publisher.value;
    }
    // title and author
    if (title && author) {
      title = title.trim();
      author = author.trim();
      this.bookService.findByTitleAndAuthor(author, title).subscribe(book => this.result = book,
        error => this.handleError(error));
      // title
    } else if (title) {
      console.log('title');
      title = title.trim();
      this.bookService.findByTitle(title).subscribe(book => this.result = book,
        error => this.handleError(error));
      // author
    } else if (author) {
      console.log('author');
      author = author.trim();
      this.bookService.findByAuthor(author).subscribe(book => this.result = book,
        error => this.handleError(error));
      // publisher
    } else if (publisher) {
      console.log('publisher');
      publisher = publisher.trim();
      this.bookService.findByPublisher(publisher).subscribe(books => this.result = books.map(book => new BookByAuthor(book)),
        error => this.handleError(error));
      // this.result = booksToMap;
    }
    this.table.dataSource = this.result;
    this.table.renderRows();
    this.result = [];
  }

  public goToBook(book: BookByAuthor): void {
    this.router.navigate([`book/${book.bookId}`]);
  }

  private goToAuthor(author: string) {
    this.router.navigate([`author/${author}`]);
  }

  ngOnInit() {
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      this.error = true;
      this.errorMessage = error.error.message;
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      this.error = true;
      let errorText = error.error.message === 'undefined' ? error.error.message : 'Unknown server error, try again later.';
      this.errorMessage = 'ERROR: ' + errorText;
      // return an observable with a user-facing error message
    }
    return throwError(
      'Something bad happened; please try again later.');
  }
}
