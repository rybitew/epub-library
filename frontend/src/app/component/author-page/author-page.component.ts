import { Component, OnInit } from '@angular/core';
import {BookByAuthor} from '../../model/book-by-author';
import {ActivatedRoute, Router} from '@angular/router';
import {BookService} from '../../service/book.service';
import {HttpErrorResponse} from '@angular/common/http';
import {throwError} from 'rxjs';

@Component({
  selector: 'app-author-page',
  templateUrl: './author-page.component.html',
  styleUrls: ['./author-page.component.css']
})
export class AuthorPageComponent implements OnInit {

  public author: string;
  //error
  public error = false;
  public errorMessage: string;
  public result: BookByAuthor[] = [];

  constructor(public router: Router, public route: ActivatedRoute, public bookService: BookService) { }

  ngOnInit() {
    this.author = this.route.snapshot.params.author;
    this.bookService.findByAuthor(this.author).subscribe(book => this.result = book,
      error => this.handleError(error));
  }

  public goToBook(id: string): void {
    this.router.navigate([`book/${id}`]);
  }

  public handleError(error: HttpErrorResponse) {
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
