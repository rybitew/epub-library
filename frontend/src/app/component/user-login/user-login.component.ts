import {Component, OnInit} from '@angular/core';
import {UserService} from '../../service/user.service';
import {UserDto} from '../../model/user-dto';
import {Router} from '@angular/router';
import {User} from '../../model/user';
import {log} from 'util';
import {throwError} from 'rxjs';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit {

  username: string;
  password: string;
  email: string;
  isLogin: boolean = true;
  error: boolean;
  errorMessage: string;

  constructor(private userService: UserService, private router: Router) {
  }

  ngOnInit() {
  }

  private login() {
    if (this.isLogin === true) {
      this.userService.validateUser(new UserDto(this.username, this.password))
        .subscribe(response => {
          console.log(response);
          if (response === true) {
            sessionStorage.setItem('user', this.username);
            sessionStorage.setItem('authenticated', 'true');
            this.router.navigate(['home']);
          }
        },
          error => this.handleError(error));
      // if (this.success === true) {
      //   sessionStorage.setItem('user', this.username);
      //   sessionStorage.setItem('authenticated', 'true');
      //   this.success = false;
      //   this.router.navigate(['home']);
      // }
    } else {
      this.isLogin = true;
    }
  }

  private register() {
    if (this.isLogin === false) {
      this.userService.registerUser(new User(this.username, this.password, this.email))
        .subscribe(response => {
          console.log(response);
          if (response === true) {
            sessionStorage.setItem('user', this.username);
            sessionStorage.setItem('authenticated', 'true');
            this.router.navigate(['home']);
          }
        },
          error => this.handleError(error));
    } else {
      this.isLogin = false;
    }
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      this.error = true;
      this.errorMessage = error.error.message;
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      this.error = true;
      this.errorMessage = error.error.message;
      // return an observable with a user-facing error message
    }
    return throwError(
      'Something bad happened; please try again later.');
  }
}
