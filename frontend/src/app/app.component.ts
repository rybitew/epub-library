import {Component} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'E-Book Library';

  constructor(public router: Router) {
  }

  ngOnInit() {
  }

  check() {
    if (sessionStorage.getItem('authenticated') === null) {
      this.router.navigate(['login']);
    } else if (sessionStorage.getItem('authenticated') === 'true') {
      sessionStorage.removeItem('user');
      sessionStorage.removeItem('authenticated');
      sessionStorage.removeItem('elevated');
      this.router.navigate(['home']);
      throw Error('Logged out');
    } else if (sessionStorage.getItem('authenticated') === 'false') {
      this.router.navigate(['login']);
    }
  }
}
