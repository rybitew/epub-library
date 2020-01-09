import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {OktaAuthModule, OktaCallbackComponent,} from '@okta/okta-angular';

import {HomeComponent} from './home/home.component';
import {MenuComponent} from './menu/menu.component';
import {RouterModule, Routes} from '@angular/router';
import {UploadComponent} from './upload/upload.component';
import {ManageAccountComponent} from './manage-account/manage-account.component';
import {UserPageComponent} from './user-page/user-page.component';
import {BookBrowserComponent} from './book-browser/book-browser.component';
import {
  MatCardModule,
  MatDialog,
  MatDialogModule,
  MatFormFieldModule,
  MatInputModule,
  MatListModule,
  MatSidenavModule, MatTableModule
} from '@angular/material';
import {AuthorBrowserComponent} from './author-browser/author-browser.component';
import {UserFinderComponent} from './user-finder/user-finder.component';
import {LoginDialogComponent} from './login-dialog/login-dialog.component';
import {AppComponent} from './app.component';
import {FormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {BookComponent} from './book/book.component';
import { ErrorHandlerComponent } from './error-handler/error-handler.component';
// import {AuthInterceptor} from './okta/auth.interceptor';

const appRoutes: Routes = [
  {path: 'home', component: HomeComponent},
  {path: 'error', component: ErrorHandlerComponent},
  {path: 'user/account', component: ManageAccountComponent},
  {path: 'book/upload', component: UploadComponent},
  {path: 'user/library', component: UserPageComponent},
  {path: 'book', component: BookBrowserComponent},
  {path: 'login/authorization', component: OktaCallbackComponent},
  {path: 'book/:id', component: BookComponent},
  {path: 'author', component: AuthorBrowserComponent},
  {path: 'user', component: UserFinderComponent},
  {path: '', redirectTo: 'home', pathMatch: 'full'}
];

const config = {
  issuer: 'https://dev-401625.okta.com/oauth2/default',
  redirectUri: 'http://localhost:4200/login/authorization',
  clientId: '0oa2fv18plLimcgcY357',
  pkce: true
};

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    MenuComponent,
    UploadComponent,
    ManageAccountComponent,
    UserPageComponent,
    BookBrowserComponent,
    AuthorBrowserComponent,
    UserFinderComponent,
    LoginDialogComponent,
    BookComponent,
    ErrorHandlerComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    MatToolbarModule,
    MatButtonModule,
    MatDialogModule,
    BrowserAnimationsModule,
    RouterModule.forRoot(appRoutes, {enableTracing: true}),
    OktaAuthModule.initAuth(config),
    MatListModule,
    MatSidenavModule,
    MatCardModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule
  ],
  providers: [
    MatDialog,
    // {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}
  ],
  bootstrap: [AppComponent],
  entryComponents: [LoginDialogComponent]
})
export class AppModule {
}
