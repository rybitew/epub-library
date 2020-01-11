import { Component, OnInit } from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {throwError} from 'rxjs';
import {User} from '../../model/user';
import {UserService} from '../../service/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-user-finder',
  templateUrl: './user-finder.component.html',
  styleUrls: ['./user-finder.component.css']
})
export class UserFinderComponent implements OnInit {

  //error
  private error = false;
  private errorMessage: string;
  private result: User;

  constructor(private router: Router, private userService: UserService) { }

  ngOnInit() {
  }

  private findUser(username) {
    if (username) {
      this.userService.getUser(username.value.trim()).subscribe(user => this.result = user,
        error => this.handleError(error));
    }
  }

  private goToUser() {
    this.router.navigate([`user/activity/${this.result.username}`])
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
      let errorText = error.error.message === 'undefined' ? error.error.message : 'Unknown server error, try again later.';
      this.errorMessage = 'ERROR: ' + errorText;
      // return an observable with a user-facing error message
    }
    return throwError(
      'Something bad happened; please try again later.');
  }
}
