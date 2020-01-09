import {Component, OnInit} from '@angular/core';
import {Book} from '../model/book';
import {ActivatedRoute} from '@angular/router';
import {BookService} from '../service/book.service';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.css']
})
export class BookComponent implements OnInit {

  private imageToShow: any;
  private isImageLoading: boolean;
  private errorImg = 'assets/images/default.png';
  private bookInfo: Book;
  private showImage: boolean;
  private imageLoaded: boolean;

  constructor(private route: ActivatedRoute, private bookService: BookService) {
  }

  ngOnInit() {
    this.bookService.findById(this.route.snapshot.params.id).subscribe(book => {
      this.bookInfo = book;
    });
  }

  private addToLibrary() {
    // this.bookService.addToUserLibrary(this.bookInfo.id, )
  }

  private deleteBook() {
    this.bookService.deleteBook(this.bookInfo.id);
  }

  private editAuthor() {

  }

  private goToAuthor(author: string) {

  }

  private goToPublisher() {

  }

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
}
