import {Component, ErrorHandler, NgZone} from '@angular/core';
import {MatSnackBar} from '@angular/material';

@Component({
  selector: 'app-error-handler',
  templateUrl: './error-handler.component.html',
  styleUrls: ['./error-handler.component.css']
})
export class ErrorHandlerComponent implements ErrorHandler {

  public errorMessage: string;

  constructor(public errorSnackBar: MatSnackBar, public zone: NgZone) {
  }

  ngOnInit() {
  }

  handleError(error: any) {
    if (error instanceof Error) {
      this.errorMessage = error.message;
    } else if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      this.errorMessage = error.error.message;
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong,
      this.errorMessage = error.error.message;
      // return an observable with a user-facing error message
    }
    if (this.errorMessage !== undefined && this.errorMessage.trim() !== '') {
      this.zone.run(() => this.errorSnackBar.open(this.errorMessage, 'Ok', {
        verticalPosition: 'bottom',
        horizontalPosition: 'center',
        duration: 1500
      }));
    }
  }

}
