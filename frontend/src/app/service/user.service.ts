import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {UserDto} from '../model/user-dto';
import {User} from '../model/user';
import {Observable} from 'rxjs';
import {BookByUserLibrary} from '../model/BookByUserLibrary';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userUrl = 'http://localhost:8082/user/';
  private loginUrl = this.userUrl + 'login';
  private registerUrl = this.userUrl + 'register';
  private deleteUrl = this.userUrl + 'delete/';
  private deleteLibraryBookUrl = this.userUrl + 'library/delete/';
  private libraryUrl = this.userUrl + 'library/';

  constructor(private http: HttpClient) {
  }

  public validateUser(user: UserDto): Observable<boolean> {
    return this.http.post<boolean>(this.loginUrl, user);
  }

  public registerUser(user: User): Observable<boolean> {
    return this.http.post<boolean>(this.registerUrl, user);
  }

  public deleteUser(): Observable<any> {
    return this.http.delete(this.deleteUrl + '?username=' + sessionStorage.getItem('user'));
  }

  public deleteBookFromLibrary(id: string): Observable<any> {
    return this.http.delete(this.deleteLibraryBookUrl + '?username=' + sessionStorage.getItem('user') + '&id=' + id);
  }

  public getUser(username: string): Observable<User> {
    return this.http.get<User>(this.userUrl + '?username=' + username);
  }

  public getUserLibrary(username: string): Observable<BookByUserLibrary[]> {
    return this.http.get<BookByUserLibrary[]>(this.libraryUrl + '?username=' + username);
  }
}

/*
@DeleteMapping(value = "/user/delete/", params = {"username"})
@DeleteMapping(value = "user/library/delete/", params = {"id", "username"})
@GetMapping(value = "/user/", params = {"username"})
@GetMapping(value = "/user/library/", params = {"username"})*/
