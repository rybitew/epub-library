import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {LoginDialogComponent} from './login-dialog/login-dialog.component';
import {OktaAuthService} from '@okta/okta-angular';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'E-Book Library';
  isAuthenticated: boolean;

  constructor(public oktaAuth: OktaAuthService) {
    // Subscribe to authentication state changes
    this.oktaAuth.$authenticationState.subscribe(
      (isAuthenticated: boolean)  => this.isAuthenticated = isAuthenticated
    );
  }

  async ngOnInit() {
    // Get the authentication state for immediate use
    this.isAuthenticated = await this.oktaAuth.isAuthenticated();
  }

  login() {
    if (!this.isAuthenticated) {
      this.oktaAuth.loginRedirect(location.href);
    } else {
      this.oktaAuth.logout('/');
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


