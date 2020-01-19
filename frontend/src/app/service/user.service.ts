import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {UserDto} from '../model/user-dto';
import {User} from '../model/user';
import {BookByUserLibrary} from '../model/book-by-user-library';
import {Observable} from 'rxjs';
// @ts-ignore
import ServerAddress from '../../assets/server-address.json';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private serverUrl: string;
  private userUrl: string;
  private loginUrl: string;
  private registerUrl: string;
  private deleteUrl: string;
  private deleteLibraryBookUrl: string;
  private libraryUrl: string;

  constructor(private http: HttpClient) {
    this.serverUrl = ServerAddress.http;
    console.log(this.serverUrl);
    this.userUrl = this.serverUrl + 'user/';
    this.loginUrl = this.userUrl + 'login';
    this.registerUrl = this.userUrl + 'register';
    this.deleteUrl = this.userUrl + 'delete/';
    this.deleteLibraryBookUrl = this.userUrl + 'library/delete/';
    this.libraryUrl = this.userUrl + 'library/';
  }

  public validateUser(user: UserDto): Observable<number> {
    return this.http.post<number>(this.loginUrl, user);
  }

  public registerUser(user: User): Observable<boolean> {
    return this.http.post<boolean>(this.registerUrl, user);
  }

  public deleteUser(): Observable<any> {
    return this.http.delete(this.deleteUrl + '?username=' + sessionStorage.getItem('user'));
  }

  public elevateUser(username: string): Observable<any> {
    return this.http.post(this.userUrl + '/elevate/', username);
  }

  public isUserElevated(username: string): Observable<boolean> {
    return this.http.get<boolean>(this.userUrl + '/elevated/?username=' + username);
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
