import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BookBrowserComponent } from './book-browser.component';

describe('BookBrowserComponent', () => {
  let component: BookBrowserComponent;
  let fixture: ComponentFixture<BookBrowserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BookBrowserComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BookBrowserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
