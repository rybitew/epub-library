import {Component, OnInit} from '@angular/core';
import {Book} from '../../model/book';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {BookService} from '../../service/book.service';
import {CommentService} from '../../service/comment.service';
import {Comment} from '../../model/comment';
import {UserService} from '../../service/user.service';
import {DeleteConfirmationDialog} from '../dialog/delete-confirmation/delete-confirmation-dialog.component';
import {MatDialog} from '@angular/material';
import {AuthorEditDialog} from '../dialog/author-edit/author-edit-dialog.component';

export interface EditAuthorsDialogData {
  authors: string[];
}

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.css']
})
export class BookComponent implements OnInit {

  public currentUser: string;
  public bookInfo: Book;
  public isInLibrary = false;
  //image
  public imageLoaded: boolean;
  public imageToShow: any;
  public isImageLoading: boolean;
  //error
  public errorImg = 'assets/images/default.png';
  //comment
  public commentContent: string;
  public comments: Comment[] = [];

  constructor(public route: ActivatedRoute, public bookService: BookService,
              public router: Router, public commentService: CommentService,
              public userService: UserService, public dialog: MatDialog) {
    this.router.routeReuseStrategy.shouldReuseRoute = function() {
      return false;
    };

    this.router.events.subscribe((evt) => {
      if (evt instanceof NavigationEnd) {
        // trick the Router into believing it's last link wasn't previously loaded
        this.router.navigated = false;
        // if you need to scroll back to top, here is the right place
        // window.scrollTo(0, 0);
      }
    });
  }

  ngOnInit() {
    this.bookService.findById(this.route.snapshot.params.id).subscribe(book => {
        this.bookInfo = book;
        this.getBookCover();
      });
    this.commentService.getBookComments(this.route.snapshot.params.id).subscribe(comments => this.comments = comments);
    if (sessionStorage.getItem('authenticated')) {
      if (sessionStorage.getItem('user')) {
        this.currentUser = sessionStorage.getItem('user');
        this.bookService.checkIfInLibrary(this.route.snapshot.params.id).subscribe(res => {
            if (res) {
              this.isInLibrary = res;
            }
          });
      }
    }
  }

  public addToLibrary() {
    if (sessionStorage.getItem('authenticated') === 'true' && sessionStorage.getItem('user')) {
      this.bookService.addToUserLibrary(this.bookInfo.id, this.bookInfo.title, this.bookInfo.authors)
        .subscribe();
      this.isInLibrary = true;
    } else {
      this.router.navigate(['login']);
    }
  }

  public removeFromLibrary() {
    if (this.isInLibrary) {
      this.userService.deleteBookFromLibrary(this.bookInfo.id)
        .subscribe();
      this.isInLibrary = false;
    }
  }

  public deleteBook() {

    this.bookService.deleteBook(this.bookInfo.id).subscribe();
    this.router.navigate(['book']);
  }

  public deleteComment(comment: Comment) {
    this.commentService.deleteComment(comment).subscribe();
    location.reload();
  }

  public isElevated() {
    return sessionStorage.getItem('authenticated') === 'true' && sessionStorage.getItem('elevated') === 'true';

  }

//region Dialogs
  public openDeleteConfirmationDialog(): void {
    if (sessionStorage.getItem('authenticated') === 'true' && sessionStorage.getItem('elevated') === 'true') {
      const dialogRef = this.dialog.open(DeleteConfirmationDialog, {
        width: '250px',
        data: {actionResult: false}
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result.actionResult === true) {
          this.deleteBook();
        }
      });
    } else {
      this.router.navigate(['login']);
    }
  }

  public openEditDialog() {
    if (sessionStorage.getItem('authenticated') === 'true' && sessionStorage.getItem('elevated') === 'true') {
      const dialogRef = this.dialog.open(AuthorEditDialog, {
        width: '290px',
        data: {authors: []}
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result.authors.authors.length > 0) {
          this.bookService.changeAuthors(this.route.snapshot.params.id, result.authors.authors)
            .subscribe();
          location.reload();
        }
      });
    } else {
      this.router.navigate(['login']);
    }
  }

//endregion

  public publish() {
    if (sessionStorage.getItem('authenticated') === 'true' && sessionStorage.getItem('user')) {
      if (this.commentContent && this.commentContent.trim()) {
        this.commentService.addComment(this.bookInfo.id, this.commentContent, this.bookInfo.title)
          .subscribe();
        this.commentContent = '';
        location.reload();
      }
    } else {
      this.router.navigate(['login']);
    }
  }

  public goToUser(user: string) {
    this.router.navigate([`user/activity/${user}`]);
  }

  public goToAuthor(author: string) {
    this.router.navigate([`author/${author}`]);
  }

  public goToPublisher(publisher: string) {
    this.router.navigate([`publisher/${publisher}`]);
  }

//region Book cover recovering
  public getBookCover() {
    this.isImageLoading = true;
    this.bookService.getImage(this.bookInfo.coverUrl).subscribe(data => {
      this.createImageFromBlob(data);
      this.isImageLoading = false;
      this.imageLoaded = true;
    }, error => {
      this.isImageLoading = false;
      this.imageToShow = this.errorImg;
      this.imageLoaded = true;
    });
  }

  public createImageFromBlob(image: Blob) {
    let reader = new FileReader();
    reader.addEventListener('load', () => {
      this.imageToShow = reader.result;
    }, false);

    if (image) {
      reader.readAsDataURL(image);
    }
  }

//endregion
}
