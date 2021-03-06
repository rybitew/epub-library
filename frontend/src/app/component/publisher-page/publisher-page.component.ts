import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {BookService} from '../../service/book.service';
import {BookByPublisher} from '../../model/book-by-publisher';

@Component({
  selector: 'app-publisher-page',
  templateUrl: './publisher-page.component.html',
  styleUrls: ['./publisher-page.component.css']
})
export class PublisherPageComponent implements OnInit {

  public publisher: string;
  //error
  public error = false;
  public errorMessage: string;
  public result: BookByPublisher[] = [];

  constructor(public router: Router, public route: ActivatedRoute, public bookService: BookService) { }

  ngOnInit() {
    this.publisher = this.route.snapshot.params.publisher;
    this.bookService.findByPublisher(this.publisher).subscribe(book => this.result = book);
  }

  public goToBook(book: BookByPublisher): void {
    this.router.navigate([`book/${book.bookId}`]);
  }

  public goToAuthor(author: string) {
    this.router.navigate([`author/${author}`]);
  }
}
