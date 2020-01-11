import {Component, OnInit} from '@angular/core';
import {Book} from '../../model/book';
import {ActivatedRoute, Router} from '@angular/router';
import {BookService} from '../../service/book.service';
import {HttpErrorResponse} from '@angular/common/http';
import {throwError} from 'rxjs';
import {CommentService} from '../../service/comment.service';
import {Comment} from '../../model/comment';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.css']
})
export class BookComponent implements OnInit {

  private bookInfo: Book;
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
              private router: Router, private commentService: CommentService) {
  }

  ngOnInit() {
    this.bookService.findById(this.route.snapshot.params.id).subscribe(book => {
      this.bookInfo = book;
      this.getBookCover();
    });
    this.commentService.getBookComments(this.route.snapshot.params.id).subscribe(comments => {
      this.comments = comments;
      // this.comments.sort((b, a) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime());
    });
  }

  private addToLibrary() {
    if (sessionStorage.getItem('authenticated') === 'true' && sessionStorage.getItem('user')) {
      this.bookService.addToUserLibrary(this.bookInfo.id, this.bookInfo.title, this.bookInfo.authors)
        .subscribe(res => console.log(res), error => this.handleError(error));
    } else {
      this.router.navigate(['login']);
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

  private publish() {
    if (sessionStorage.getItem('authenticated') === 'true' && sessionStorage.getItem('user')) {
      if (this.commentContent.trim()) {
        console.log('commented');
        this.commentService.addComment(this.bookInfo.id, this.commentContent)
          .subscribe(res => console.log(res), error => this.handleError(error));
        this.commentContent = '';
        this.router.navigateByUrl(location.pathname, { skipLocationChange: true }).then(() => {
          this.router.navigate([location.pathname]);
        });
      }
    } else {
      this.router.navigate(['login']);
    }
  }

  private goToAuthor(author: string) {
    this.router.navigate(['author/page/'], {queryParams: {'author': author}});
  }

  private goToPublisher(publisher: string) {
    this.router.navigate(['publisher/page/'], {queryParams: {'author': publisher}});
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
