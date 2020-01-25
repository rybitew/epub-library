import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Book} from '../model/book';
import {BookByAuthor} from '../model/book-by-author';
import {BookByPublisher} from '../model/book-by-publisher';
import {Observable} from 'rxjs';
// @ts-ignore
import ServerAddress from '../../assets/server-address.json';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  private serverUrl: string;
  private authorUrl: string;
  private publisherUrl: string;
  private bookUrl: string;

  constructor(private http: HttpClient) {
    this.serverUrl = ServerAddress.http;
    this.authorUrl = this.serverUrl + 'authors';
    this.publisherUrl = this.serverUrl + 'publishers';
    this.bookUrl = this.serverUrl + 'books';
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

  public findByTitleAndPublisher(title, publisher): Observable<BookByAuthor[]> {
    return this.http.get<BookByAuthor[]>(this.bookUrl.concat('/?publisher=', publisher, '&title=', title));
  }

  public findByAuthorAndPublisher(author, publisher): Observable<BookByAuthor[]> {
    return this.http.get<BookByAuthor[]>(this.bookUrl.concat('/?author=', author, '&publisher=', publisher));
  }

  public findByTitleAuthorAndPublisher(title, author, publisher): Observable<BookByAuthor[]> {
    return this.http.get<BookByAuthor[]>(this.bookUrl.concat('/?author=', author, '&title=', title, '&publisher=', publisher));
  }

  public checkIfInLibrary(bookId: string): Observable<boolean> {
    return this.http.get<boolean>(this.bookUrl.concat('/library/?id=', bookId, '&username=', sessionStorage.getItem('user')));
  }

  public getImage(imageUrl: string): Observable<Blob> {
    return this.http.get(this.bookUrl.concat('/cover/?path=', imageUrl), {responseType: 'blob'});
  }

  // post
  public changeAuthors(id: string, authors: string[]): Observable<any> {
    let book: BookByPublisher = new BookByPublisher();
    book.bookId = id;
    book.authors = authors;
    console.log(book);

    return this.http.put(this.bookUrl.concat('/change-author/'), book);
  }

  public addToUserLibrary(id: string, title: string, authors: string[]): Observable<any> {
    return this.http.post(this.bookUrl.concat(
      '/add-to-library/?id=', id, '&user=', sessionStorage.getItem('user'), '&title=', title), authors);
  }

  // delete
  public deleteBook(id: string): Observable<any> {
    return this.http.delete(this.bookUrl.concat('/delete/?id=', id));
  }
}
