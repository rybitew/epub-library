import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {DeleteConfirmationDialogData} from '../../user-page/user-page.component';
import {UserService} from '../../../service/user.service';
import {Router} from '@angular/router';
import {FormArray, FormBuilder, FormGroup} from '@angular/forms';
import {EditAuthorsDialogData} from '../../book/book.component';

@Component({
  selector: 'app-author-edit-dialog',
  templateUrl: './author-edit-dialog.component.html',
  styleUrls: ['./author-edit-dialog.component.css']
})
export class AuthorEditDialog implements OnInit {

  myForm: FormGroup;

  constructor(public dialogRef: MatDialogRef<AuthorEditDialog>,
              @Inject(MAT_DIALOG_DATA) public data: EditAuthorsDialogData,
              public userService: UserService,
              public router: Router,
              public fb: FormBuilder) {
    this.createForm();
  }

  ngOnInit() {
  }

  onNoClick() {
    this.data.authors = [];
    this.dialogRef.close({actionResult: this.data});
  }

  onConfirmClick() {
    for (let i = 0; i < this.things.length; i++) {
      if (this.things.at(i).value !== '') {
        this.data.authors.push(this.things.at(i).value);
      }
    }
    this.dialogRef.close({authors: this.data});
  }

  onEnter() {
    this.addThing();
  }

  get things() {
    return this.myForm.get('things') as FormArray;
  }

  public createForm() {
    this.myForm = this.fb.group({
      things: this.fb.array([
        this.fb.control('')
      ])
    });
  }

  public addThing() {
    this.things.push(this.fb.control(''));
  }

  public delete(index) {
    this.things.removeAt(index);
  }

}
