import {BrowserModule} from '@angular/platform-browser';
import {ErrorHandler, NgModule} from '@angular/core';

import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import {HomeComponent} from './component/home/home.component';
import {MenuComponent} from './component/menu/menu.component';
import {RouterModule, Routes} from '@angular/router';
import {UserPageComponent} from './component/user-page/user-page.component';
import {BookBrowserComponent} from './component/book-browser/book-browser.component';
import {
  MatCardModule,
  MatDialog,
  MatDialogModule,
  MatFormFieldModule,
  MatInputModule,
  MatListModule,
  MatSidenavModule, MatSnackBar, MatSnackBarContainer, MatSnackBarModule, MatTableModule
} from '@angular/material';
import {UserFinderComponent} from './component/user-finder/user-finder.component';
import {AppComponent} from './app.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {BookComponent} from './component/book/book.component';
import {UserLoginComponent} from './component/user-login/user-login.component';
import {AuthorPageComponent} from './component/author-page/author-page.component';
import {PublisherPageComponent} from './component/publisher-page/publisher-page.component';
import {DeleteConfirmationDialog} from './component/dialog/delete-confirmation/delete-confirmation-dialog.component';
import {AuthorEditDialog} from './component/dialog/author-edit/author-edit-dialog.component';
import {ErrorHandlerComponent} from './component/error-handler/error-handler.component';

const appRoutes: Routes = [
  {path: 'home', component: HomeComponent},
  {path: 'user/activity/:username', component: UserPageComponent},
  {path: 'book', component: BookBrowserComponent},
  {path: 'book/:id', component: BookComponent},
  {path: 'author/:author', component: AuthorPageComponent},
  {path: 'publisher/:publisher', component: PublisherPageComponent},
  {path: 'user', component: UserFinderComponent},
  {path: 'login', component: UserLoginComponent},
  {path: '', redirectTo: 'home', pathMatch: 'full'}
];

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    MenuComponent,
    UserPageComponent,
    BookBrowserComponent,
    UserFinderComponent,
    BookComponent,
    UserLoginComponent,
    AuthorPageComponent,
    PublisherPageComponent,
    DeleteConfirmationDialog,
    AuthorEditDialog,
    ErrorHandlerComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    MatToolbarModule,
    MatButtonModule,
    MatDialogModule,
    BrowserAnimationsModule,
    RouterModule.forRoot(appRoutes, {
      enableTracing: true,
      onSameUrlNavigation: 'reload'
    }),
    MatListModule,
    MatSidenavModule,
    MatCardModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    ReactiveFormsModule,
    MatSnackBarModule
  ],
  entryComponents: [
    DeleteConfirmationDialog,
    AuthorEditDialog
  ],
  providers: [
    MatDialog,
    {
      provide: ErrorHandler,
      useClass: ErrorHandlerComponent
    }
  ],
  bootstrap: [
    AppComponent,
    ErrorHandlerComponent
  ],
})
export class AppModule {
}
