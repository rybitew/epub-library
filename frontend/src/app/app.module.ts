import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import {HomeComponent} from './component/home/home.component';
import {MenuComponent} from './component/menu/menu.component';
import {RouterModule, Routes} from '@angular/router';
import {UploadComponent} from './component/upload/upload.component';
import {UserPageComponent} from './component/user-page/user-page.component';
import {BookBrowserComponent} from './component/book-browser/book-browser.component';
import {
  MatCardModule,
  MatDialog,
  MatDialogModule,
  MatFormFieldModule,
  MatInputModule,
  MatListModule,
  MatSidenavModule, MatTableModule
} from '@angular/material';
import {UserFinderComponent} from './component/user-finder/user-finder.component';
import {AppComponent} from './app.component';
import {FormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {BookComponent} from './component/book/book.component';
import {UserLoginComponent} from './component/user-login/user-login.component';
import {AuthorPageComponent} from './component/author-page/author-page.component';
import {PublisherPageComponent} from './component/publisher-page/publisher-page.component';
import { DeleteConfirmationDialog } from './component/user-page/delete-confirmation/delete-confirmation-dialog.component';

const appRoutes: Routes = [
  {path: 'home', component: HomeComponent},
  {path: 'book/upload', component: UploadComponent},
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
    UploadComponent,
    UserPageComponent,
    BookBrowserComponent,
    UserFinderComponent,
    BookComponent,
    UserLoginComponent,
    AuthorPageComponent,
    PublisherPageComponent,
    DeleteConfirmationDialog,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    MatToolbarModule,
    MatButtonModule,
    MatDialogModule,
    BrowserAnimationsModule,
    RouterModule.forRoot(appRoutes, {enableTracing: true}),
    MatListModule,
    MatSidenavModule,
    MatCardModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule
  ],
  entryComponents: [
    DeleteConfirmationDialog
  ],
  providers: [
    MatDialog
  ],
  bootstrap: [AppComponent],
})
export class AppModule {
}
