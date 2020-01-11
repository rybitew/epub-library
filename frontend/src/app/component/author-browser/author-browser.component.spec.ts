import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthorBrowserComponent } from './author-browser.component';

describe('AuthorBrowserComponent', () => {
  let component: AuthorBrowserComponent;
  let fixture: ComponentFixture<AuthorBrowserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AuthorBrowserComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthorBrowserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
