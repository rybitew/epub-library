import {Component, OnInit, ViewChild} from '@angular/core';
import {MatDialog, MatTable} from '@angular/material';
import {BookByAuthor} from '../../model/book-by-author';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';
import {throwError} from 'rxjs';
import {UserService} from '../../service/user.service';
import {BookByUserLibrary} from '../../model/book-by-user-library';
import {CommentService} from '../../service/comment.service';
import {Comment} from '../../model/comment';
import {DeleteConfirmationDialog} from './delete-confirmation/delete-confirmation-dialog.component';

export interface DialogData {
  username: string;
}

@Component({
  selector: 'app-my-library',
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.css']
})
export class UserPageComponent implements OnInit {

  @ViewChild('table', {static: false}) table: MatTable<BookByUserLibrary>;

  private authenticated: boolean;
  private username: string;
  private currentUser: string;
  //error
  private error = false;
  private errorMessage: string;
  //library
  private result: BookByUserLibrary[] = [];
  private displayedColumns = ['index', 'title', 'authors', 'delete'];
  //comments
  private comments: Comment[] = [];

  constructor(private router: Router, private route: ActivatedRoute,
              private userService: UserService, private commentService: CommentService,
              public dialog: MatDialog) {
  }

  ngOnInit() {
    // if (sessionStorage.getItem('authenticated') !== 'true') {
    //
    // }
    this.authenticated = sessionStorage.getItem('authenticated') === 'true';
    this.username = this.route.snapshot.params.username;
    this.currentUser = sessionStorage.getItem('user');
    this.commentService.getUserComments(this.username).subscribe(comments => {
      this.comments = comments;
    });
    this.showResults();
  }

  private showResults() {
    this.userService.getUserLibrary(this.username.trim()).subscribe(book => this.result = book,
      error => this.handleError(error));
    this.table.dataSource = this.result;
    this.table.renderRows();
    this.result = [];
  }

  private deleteComment(comment: Comment) {
    this.commentService.deleteComment(comment).subscribe(res => console.log(res),
      error => this.handleError(error));
    if (this.error === false) {
      location.reload();
    }
  }

  private deleteFromLibrary(id: string) {
    console.log(id);
    this.userService.deleteBookFromLibrary(id).subscribe(res => console.log(res),
      error => this.handleError(error));
    if (this.error === false) {
      location.reload();
    }
  }

  private openDeleteConfirmationDialog(): void {
    const dialogRef = this.dialog.open(DeleteConfirmationDialog, {
      width: '250px',
      data: {username: this.currentUser}
    });
  }

  private goToBook(id: string): void {
    this.router.navigate([`book/${id}`]);
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
      this.errorMessage = error.error.message;
      // return an observable with a user-facing error message
    }
    return throwError(
      'Something bad happened; please try again later.');
  }
}
