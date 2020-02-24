import {Component, OnInit, ViewChild} from '@angular/core';
import {MatDialog, MatTable} from '@angular/material';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '../../service/user.service';
import {BookByUserLibrary} from '../../model/book-by-user-library';
import {CommentService} from '../../service/comment.service';
import {Comment} from '../../model/comment';
import {DeleteConfirmationDialog} from '../dialog/delete-confirmation/delete-confirmation-dialog.component';

export interface DeleteConfirmationDialogData {
  actionResult: boolean;
}

@Component({
  selector: 'app-my-library',
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.css']
})
export class UserPageComponent implements OnInit {

  @ViewChild('table', {static: false}) table: MatTable<BookByUserLibrary>;

  public elevated = false;
  public deleteConfirmation = false;
  public authenticated: boolean;
  public username: string;
  public currentUser: string;
  //error
  public error = false;
  //library
  public result: BookByUserLibrary[] = [];
  public displayedColumns = ['index', 'title', 'authors', 'delete'];
  //comments
  public comments: Comment[] = [];

  constructor(public router: Router, public route: ActivatedRoute,
              public userService: UserService, public commentService: CommentService,
              public dialog: MatDialog) {
  }

  ngOnInit() {
    this.authenticated = sessionStorage.getItem('authenticated') === 'true';
    this.username = this.route.snapshot.params.username;
    this.currentUser = sessionStorage.getItem('user');
    this.commentService.getUserComments(this.username).subscribe(comments => {
      this.comments = comments;
    });
    this.showResults();
    this.isUserElevated();
  }

  public showResults() {
    this.userService.getUserLibrary(this.username.trim()).subscribe(book => {
      this.table.dataSource = book;
      this.table.renderRows();
    });
    this.result = [];
  }

  public deleteComment(comment: Comment) {
    this.commentService.deleteComment(comment).subscribe();
    location.reload();
  }

  public deleteFromLibrary(id: string) {
    this.userService.deleteBookFromLibrary(id).subscribe();
    location.reload();
  }

  public openDeleteConfirmationDialog(): void {
    if (sessionStorage.getItem('authenticated') === 'true' && sessionStorage.getItem('elevated') === 'true') {
      const dialogRef = this.dialog.open(DeleteConfirmationDialog, {
        width: '250px',
        data: {actionResult: this.deleteConfirmation}
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result.actionResult === true) {
          this.deleteAccount();
        }
      });
    }
  }

  public deleteAccount() {
    this.userService.deleteUser(this.username).subscribe();
    this.router.navigate(['home']);
  }

  public elevateUser() {
    this.userService.elevateUser(this.username).subscribe();
    this.elevated = true;
  }

  public isUserElevated() {
    this.userService.isUserElevated(this.username).subscribe(res => {
      this.elevated = res;
    });
  }

  public isElevated() {
    return sessionStorage.getItem('authenticated') === 'true' && sessionStorage.getItem('elevated') === 'true';
  }

  public goToBook(id: string): void {
    this.router.navigate([`book/${id}`]);
  }

  public goToAuthor(author: string) {
    this.router.navigate([`author/${author}`]);
  }
}
