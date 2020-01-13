import {Component, Inject, OnInit} from '@angular/core';
import {BookByAuthor} from '../../model/book-by-author';
import {ActivatedRoute, Router} from '@angular/router';
import {BookService} from '../../service/book.service';
import {HttpErrorResponse} from '@angular/common/http';
import {throwError} from 'rxjs';
import {BookByPublisher} from '../../model/book-by-publisher';
import {MAT_DIALOG_DATA} from '@angular/material';

@Component({
  selector: 'app-publisher-page',
  templateUrl: './publisher-page.component.html',
  styleUrls: ['./publisher-page.component.css']
})
export class PublisherPageComponent implements OnInit {

  private publisher: string;
  //error
  private error = false;
  private errorMessage: string;
  private result: BookByPublisher[] = [];

  constructor(private router: Router, private route: ActivatedRoute, private bookService: BookService) { }

  ngOnInit() {
    this.publisher = this.route.snapshot.params.publisher;
    this.bookService.findByPublisher(this.publisher).subscribe(book => this.result = book,
      error => this.handleError(error));
  }

  public goToBook(book: BookByPublisher): void {
    this.router.navigate([`book/${book.bookId}`]);
  }

  private goToAuthor(author: string) {
    this.router.navigate([`author/${author}`]);
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
