import { Component, OnInit } from '@angular/core';
import {User} from '../../model/user';
import {UserService} from '../../service/user.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-user-finder',
  templateUrl: './user-finder.component.html',
  styleUrls: ['./user-finder.component.css']
})
export class UserFinderComponent implements OnInit {

  public result: User;

  constructor(public router: Router, public userService: UserService) { }

  ngOnInit() {
  }

  public findUser(username) {
    if (username && username.value.trim() !== '') {
      this.userService.getUser(username.value.trim()).subscribe(user => this.result = user);
    }
  }

  public goToUser() {
    this.router.navigate([`user/activity/${this.result.username}`])
  }
}
