import {Component, OnInit, ViewChild} from '@angular/core';
import {MatTable} from '@angular/material';
import {BookByAuthor} from '../../model/book-by-author';
import {Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';
import {throwError} from 'rxjs';
import {UserService} from '../../service/user.service';
import {BookByUserLibrary} from '../../model/BookByUserLibrary';

@Component({
  selector: 'app-my-library',
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.css']
})
export class UserPageComponent implements OnInit {

  @ViewChild('table', {static: false}) table: MatTable<BookByUserLibrary>;

  private error = false;
  private errorMessage: string;
  private username: string;
  private result: BookByUserLibrary[] = [];
  private displayedColumns = ['index', 'title', 'authors'];

  constructor(private router: Router, private userService: UserService) {
  }

  ngOnInit() {
    // if (sessionStorage.getItem('authenticated') !== 'true') {
    //
    // }
    this.username = sessionStorage.getItem('user');
    console.log(sessionStorage.getItem('user'));
    this.showResults();
  }

  private showResults() {
    this.userService.getUserLibrary(this.username.trim()).subscribe(book => this.result = book,
      error => this.handleError(error));
    this.table.dataSource = this.result;
    this.table.renderRows();
    this.result = [];
  }

  private goToBook(book: BookByAuthor): void {
    this.router.navigate([`book/${book.bookId}`]);
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
      this.errorMessage = error.error.message;
      // return an observable with a user-facing error message
    }
    return throwError(
      'Something bad happened; please try again later.');
  }
}
