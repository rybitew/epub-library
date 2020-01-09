import {Component, ElementRef, NgZone, OnInit, ViewChild} from '@angular/core';
import {BookService} from '../service/book.service';
import {BookByAuthor} from '../model/book-by-author';
import {MatTable, MatTableDataSource} from '@angular/material';
import {Router} from '@angular/router';
import {error} from 'util';

@Component({
  selector: 'app-book-browser',
  templateUrl: './book-browser.component.html',
  styleUrls: ['./book-browser.component.css']
})
export class BookBrowserComponent implements OnInit {

  @ViewChild('table', {static: false}) table: MatTable<BookByAuthor>;

  private result: BookByAuthor[] = [];
  private displayedColumns = ['index', 'title', 'authors'];

  constructor(private bookService: BookService, private router: Router) {}

  public showResults(title, author, publisher): void {
    if (title) { title = title.value; }
    if (author) { author = author.value; }
    if (publisher) { publisher = publisher.value; }
    // title and author
    if (title && author) {
      title = title.trim();
      author = author.trim();
      this.bookService.findByTitleAndAuthor(author, title).subscribe(book => this.result = book);
      // title
    } else if (title) {
      console.log('title');
      title = title.trim();
      this.bookService.findByTitle(title).subscribe(book => this.result = book);
      // author
    } else if (author) {
      console.log('author');
      author = author.trim();
      this.bookService.findByAuthor(author).subscribe(book => this.result = book);
      // publisher
    } else if (publisher) {
      console.log('publisher');
      publisher = publisher.trim();
      this.bookService.findByPublisher(publisher).subscribe(books => this.result = books.map(book => new BookByAuthor(book)));
      // this.result = booksToMap;
    }
    this.table.dataSource = this.result;
    this.table.renderRows();
    this.result = [];
  }
  public goToBook(book: BookByAuthor): void {
    const success = this.router.navigate([`book/${book.bookId}`]);
    if (!success) { error('Could not redirect.'); }
  }

  ngOnInit() {
  }
}
