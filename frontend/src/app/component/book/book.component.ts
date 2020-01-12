import {Component, OnInit} from '@angular/core';
import {Book} from '../../model/book';
import {ActivatedRoute, Router} from '@angular/router';
import {BookService} from '../../service/book.service';
import {HttpErrorResponse} from '@angular/common/http';
import {throwError} from 'rxjs';
import {CommentService} from '../../service/comment.service';
import {Comment} from '../../model/comment';
import {UserService} from '../../service/user.service';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.css']
})
export class BookComponent implements OnInit {

  private currentUser: string;
  private bookInfo: Book;
  private isInLibrary = false;
  //image
  private showImage: boolean;
  private imageLoaded: boolean;
  private imageToShow: any;
  private isImageLoading: boolean;
  //error
  private error = false;
  private errorMessage: string;
  private errorImg = 'assets/images/default.png';
  //comment
  private commentContent: string;
  private comments: Comment[] = [];

  constructor(private route: ActivatedRoute, private bookService: BookService,
              private router: Router, private commentService: CommentService,
              private userService: UserService) {
  }

  ngOnInit() {
    this.bookService.findById(this.route.snapshot.params.id).subscribe(book => {
        this.bookInfo = book;
        this.getBookCover();
      },
      error => this.handleError(error));
    this.commentService.getBookComments(this.route.snapshot.params.id).subscribe(comments => this.comments = comments,
      error => this.handleError(error));
    if (sessionStorage.getItem('authenticated')) {
      if (sessionStorage.getItem('user')) {
        this.currentUser = sessionStorage.getItem('user');
        this.bookService.checkIfInLibrary(this.route.snapshot.params.id).subscribe(res => {
            if (res) {
              this.isInLibrary = res;
            }
          },
          error => this.handleError(error));
      }
    }
    console.log(this.isInLibrary);
  }

  private addToLibrary() {
    if (sessionStorage.getItem('authenticated') === 'true' && sessionStorage.getItem('user')) {
      this.bookService.addToUserLibrary(this.bookInfo.id, this.bookInfo.title, this.bookInfo.authors)
        .subscribe(res => console.log(res), error => this.handleError(error));
      this.isInLibrary = true;
    } else {
      this.router.navigate(['login']);
    }
  }

  private removeFromLibrary() {
    if (this.isInLibrary) {
      this.userService.deleteBookFromLibrary(this.bookInfo.id)
        .subscribe(res => console.log(res), error => this.handleError(error));
      this.isInLibrary = false;
    }
  }

  private deleteBook() {
    if (sessionStorage.getItem('authenticated') === 'true') {
      this.bookService.deleteBook(this.bookInfo.id).subscribe(res => console.log(res), error => this.handleError(error));
      this.router.navigate(['book']);
    } else {
      this.router.navigate(['login']);
    }
  }

  private deleteComment(comment: Comment) {
    this.commentService.deleteComment(comment).subscribe(res => console.log(res),
      error => this.handleError(error));
    if (this.error === false) {
      location.reload();
    }
  }

  private publish() {
    if (sessionStorage.getItem('authenticated') === 'true' && sessionStorage.getItem('user')) {
      if (this.commentContent.trim()) {
        console.log('commented');
        this.commentService.addComment(this.bookInfo.id, this.commentContent, this.bookInfo.title)
          .subscribe(res => console.log(res), error => this.handleError(error));
        this.commentContent = '';
        location.reload();
      }
    } else {
      this.router.navigate(['login']);
    }
  }

  private goToUser(user: string) {
    this.router.navigate([`user/activity/${user}`]);
  }

  private goToAuthor(author: string) {
    this.router.navigate([`author/${author}`]);
  }

  private goToPublisher(publisher: string) {
    this.router.navigate([`publisher/${publisher}`]);
  }

//region Book cover recovering
  private show() {
    this.showImage = true;
    if (!this.imageLoaded) {
      this.getBookCover();
    }
    console.log(this.bookInfo.releaseDate);
  }

  private getBookCover() {
    console.log(this.bookInfo);
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

  private createImageFromBlob(image: Blob) {
    let reader = new FileReader();
    reader.addEventListener('load', () => {
      this.imageToShow = reader.result;
    }, false);

    if (image) {
      reader.readAsDataURL(image);
    }
  }

//endregion

//region Error handling
  private showError(msg: string) {
    this.error = true;
    this.errorMessage = msg;
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

//endregion
}
