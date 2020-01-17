import {Component, ElementRef, NgZone, OnInit, ViewChild} from '@angular/core';
import {BookService} from '../../service/book.service';
import {BookByAuthor} from '../../model/book-by-author';
import {MatTable, MatTableDataSource} from '@angular/material';
import {Router} from '@angular/router';
import {error} from 'util';
import {HttpErrorResponse} from '@angular/common/http';
import {throwError} from 'rxjs';

@Component({
  selector: 'app-book-browser',
  templateUrl: './book-browser.component.html',
  styleUrls: ['./book-browser.component.css']
})
export class BookBrowserComponent implements OnInit {

  @ViewChild('table', {static: false}) table: MatTable<BookByAuthor>;

  public result: BookByAuthor[] = [];
  public displayedColumns = ['index', 'title', 'authors', 'publisher'];

  constructor(public bookService: BookService, public router: Router) {
  }

  public showResults(title, author, publisher): void {
    if (title) {
      title = title.value.trim();
    }
    if (author) {
      author = author.value.trim();
    }
    if (publisher) {
      publisher = publisher.value.trim();
    }
    // title and author
    if (title && author) {
      if (title !== '' && author != '') {
        this.bookService.findByTitleAndAuthor(author, title).subscribe(book => this.result = book);
      }
      // title
    } else if (title) {
      if (title !== '') {
        this.bookService.findByTitle(title).subscribe(book => this.result = book);
      }
      // author
    } else if (author) {
      if (author != '') {
        this.bookService.findByAuthor(author).subscribe(book => {
          this.result = book;
        });
      }
      // publisher
    } else if (publisher) {
      if (publisher != '') {
        this.bookService.findByPublisher(publisher).subscribe(books => this.result = books.map(book => new BookByAuthor(book)));
      }
    }
    if (this.result !== undefined) {
      if (this.table) {
        console.log(this.result);
        this.table.dataSource = this.result;
        this.table.renderRows();
        this.result = [];
      }
    }
  }

  public goToBook(id: string): void {
    this.router.navigate([`book/${id}`]);
  }

  public goToAuthor(author: string) {
    this.router.navigate([`author/${author}`]);
  }

  public goToPublisher(publisher: string) {
    this.router.navigate([`publisher/${publisher}`]);
  }

  ngOnInit() {
  }
}
