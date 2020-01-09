import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Book} from '../model/book';
import {BookByAuthor} from '../model/book-by-author';
import {BookByPublisher} from '../model/book-by-publisher';
import {catchError} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  private authorUrl = 'http://localhost:8082/authors';
  private publisherUrl = 'http://localhost:8082/publishers';
  private bookUrl = 'http://localhost:8082/books';

  constructor(private http: HttpClient) {
  }

  // get
  public findById(id: string): Observable<Book> {
    return this.http.get<Book>(this.bookUrl.concat('/?id=', id));
  }

  public findByTitle(title): Observable<BookByAuthor[]> {
    return this.http.get<BookByAuthor[]>(this.bookUrl.concat('/?title=', title));
  }

  public findByAuthor(author): Observable<BookByAuthor[]> {
    return this.http.get<BookByAuthor[]>(this.authorUrl.concat('/?author=', author));
  }

  public findByPublisher(publisher): Observable<BookByPublisher[]> {
    return this.http.get<BookByPublisher[]>(this.bookUrl.concat('/?publisher=', publisher));
  }

  public findByTitleAndAuthor(author, title): Observable<BookByAuthor[]> {
    return this.http.get<BookByAuthor[]>(this.bookUrl.concat('/?author=', author, '&title=', title));
  }

  public findAllAuthors(): Observable<string[]> {
    return this.http.get<string[]>(this.authorUrl.concat('/all'));
  }

  public findAllPublishers(): Observable<string[]> {
    return this.http.get<string[]>(this.publisherUrl.concat('/all'));
  }

  public getImage(imageUrl: string): Observable<Blob> {
    return this.http.get(this.bookUrl.concat('/cover/?path=', imageUrl), {responseType: 'blob'});
  }

  // set
  public changeAuthors(id: string, authors: string[]): void {
    let authorList = authors.toString();
    authorList.replace(' ', '%20');
    this.http.post(this.bookUrl.concat('/?id=', id, '&author=', authorList), null);
  }

  public addToUserLibrary(id: string, user: string, title: string): void {
    this.http.post(this.bookUrl.concat('/add-to-library/?id=', id, '&user=', user, '&title=', title), null);
  }

  // delete
  public deleteBook(id: string): void {
    this.http.delete(this.bookUrl.concat('/delete/?id=', id));
  }
}
