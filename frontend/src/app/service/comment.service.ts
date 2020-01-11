import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Comment} from '../model/comment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private serverUrl = 'http://localhost:8082/';
  private addCommentUrl = this.serverUrl + 'book/comment/add/';
  private deleteCommentUrl = this.serverUrl + 'book/comment/delete/';
  private getUserCommentsUrl = this.serverUrl + 'user/comment/get/';
  private getBookCommentsUrl = this.serverUrl + 'book/comment/get/';

  constructor(private http: HttpClient) {
  }

  public addComment(bookId: string, comment: string): Observable<any> {
    return this.http.post<Comment>(this.addCommentUrl, new Comment(sessionStorage.getItem('user'), bookId, comment));
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

