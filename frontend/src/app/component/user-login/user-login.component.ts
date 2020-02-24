import {Component, OnInit} from '@angular/core';
import {UserService} from '../../service/user.service';
import {UserDto} from '../../model/user-dto';
import {Router} from '@angular/router';
import {User} from '../../model/user';

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

  constructor(public userService: UserService, public router: Router) {
  }

  ngOnInit() {
  }

  public login() {
    if (this.isLogin === true) {
      if (!this.username || !this.password || this.username.trim() === '' || this.password.trim() === '') {
        throw new Error("Please fill all fields.");
      }
      this.userService.validateUser(new UserDto(this.username.trim(), this.password.trim()))
        .subscribe(response => {
          if (response === 0) {
            sessionStorage.setItem('user', this.username.trim());
            sessionStorage.setItem('authenticated', 'true');
            this.router.navigate(['home']);
          } else if (response === 1) {
            sessionStorage.setItem('user', this.username.trim());
            sessionStorage.setItem('authenticated', 'true');
            sessionStorage.setItem('elevated', 'true');
            this.router.navigate(['home']);
          }
        });
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

  public register() {
    if (this.isLogin === false) {
      if (!this.username || !this.password || !this.email
        ||  this.username.trim() === '' || this.password.trim() === '' || this.email.trim() === '') {
        throw new Error("Please fill all fields.");
      }
      this.userService.registerUser(new User(this.username.trim(), this.password.trim(), this.email.trim()))
        .subscribe(response => {
          if (response === true) {
            sessionStorage.setItem('user', this.username.trim());
            sessionStorage.setItem('authenticated', 'true');
            this.router.navigate(['home']);
          }
        });
    } else {
      this.isLogin = false;
    }
  }
}
