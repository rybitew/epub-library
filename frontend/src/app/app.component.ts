import {Component, Inject} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'E-Book Library';

  constructor(private router: Router) {
  }

  ngOnInit() {
  }

  check() {
    if (sessionStorage.getItem('authenticated') === null) {
      this.router.navigate(['login']);
    } else if (sessionStorage.getItem('authenticated') === 'true') {
      sessionStorage.removeItem('authenticated');
      this.router.navigate(['home']);
    } else if (sessionStorage.getItem('authenticated') === 'false') {
      this.router.navigate(['login']);
    }
  }
}

// username: string;
// password: string;
//
// constructor(public dialog: MatDialog) {}
//
// openDialog(): void {
//   const dialogRef = this.dialog.open(LoginDialogComponent, {
//     width: '250px',
//     data: {username: this.username, password: this.password}
//   });
//
// dialogRef.afterClosed().subscribe(result => {
//   console.log('The dialog was closed');
// });
// }


