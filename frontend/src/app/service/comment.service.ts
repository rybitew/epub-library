import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Comment} from '../model/comment';
// @ts-ignore
import ServerAddress from '../../assets/server-address.json';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private serverUrl: string;
  private addCommentUrl: string;
  private deleteCommentUrl: string;
  private getUserCommentsUrl: string;
  private getBookCommentsUrl: string;

  constructor(private http: HttpClient) {
  this.serverUrl = ServerAddress.http;
  this.addCommentUrl = this.serverUrl + 'book/comment/add/';
  this.deleteCommentUrl = this.serverUrl + 'book/comment/delete/';
  this.getUserCommentsUrl = this.serverUrl + 'user/comment/get/';
  this.getBookCommentsUrl = this.serverUrl + 'book/comment/get/';
  }

  public addComment(bookId: string, comment: string, title: string): Observable<any> {
    return this.http.post<Comment>(this.addCommentUrl, new Comment(sessionStorage.getItem('user'), bookId, comment, title));
  }

  public deleteComment(comment: Comment): Observable<any> {
    const options = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      body: {
        id: comment.id,
        username: comment.username,
        bookId: comment.bookId,
        timestamp: comment.timestamp,
        comment: null,
      },
    };
    console.log('delete');
    return this.http.delete(this.deleteCommentUrl, options);
  }

  public getUserComments(username: string): Observable<Comment[]> {
    return this.http.get<Comment[]>(this.getUserCommentsUrl + '?username=' + username);
  }
  public getBookComments(bookId: string): Observable<Comment[]> {
    return this.http.get<Comment[]>(this.getBookCommentsUrl + '?id=' + bookId);
  }
}

