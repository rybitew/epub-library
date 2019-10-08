import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserLibraryComponent } from './user-library.component';

describe('MyLibraryComponent', () => {
  let component: UserLibraryComponent;
  let fixture: ComponentFixture<UserLibraryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserLibraryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserLibraryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
