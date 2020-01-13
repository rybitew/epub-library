import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthorEditDialog } from './author-edit-dialog.component';

describe('AuthorEditDialogComponent', () => {
  let component: AuthorEditDialog;
  let fixture: ComponentFixture<AuthorEditDialog>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AuthorEditDialog ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthorEditDialog);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
