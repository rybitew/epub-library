import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {UserService} from '../../../service/user.service';
import {Router} from '@angular/router';
import {DeleteConfirmationDialogData} from '../../user-page/user-page.component';

@Component({
  selector: 'app-delete-confirmation',
  templateUrl: './delete-confirmation-dialog.component.html',
  styleUrls: ['./delete-confirmation-dialog.component.css']
})
export class DeleteConfirmationDialog implements OnInit {

  constructor(public dialogRef: MatDialogRef<DeleteConfirmationDialog>,
              @Inject(MAT_DIALOG_DATA) public data: DeleteConfirmationDialogData,
              private userService: UserService,
              private router: Router) {
  }

  onNoClick() {
    this.data.actionResult = false;
    this.dialogRef.close({actionResult: false});
  }

  onConfirmClick() {
    this.data.actionResult = true;
    this.dialogRef.close({actionResult: true});
  }

  ngOnInit() {
  }

}
