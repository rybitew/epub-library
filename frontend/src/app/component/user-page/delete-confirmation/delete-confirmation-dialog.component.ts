import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {DialogData} from '../user-page.component';
import {UserService} from '../../../service/user.service';
import {log} from 'util';
import {Router} from '@angular/router';

@Component({
  selector: 'app-delete-confirmation',
  templateUrl: './delete-confirmation-dialog.component.html',
  styleUrls: ['./delete-confirmation-dialog.component.css']
})
export class DeleteConfirmationDialog implements OnInit {

  constructor(public dialogRef: MatDialogRef<DeleteConfirmationDialog>,
              private userSevice: UserService,
              private router: Router) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  deleteAccount() {
    this.userSevice.deleteUser().subscribe(res => console.log(res));
    sessionStorage.removeItem('user');
    sessionStorage.removeItem('authenticated');
    this.onNoClick();
    this.router.navigate(['home']);
  }

  ngOnInit() {
  }

}
