import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FlexLayoutModule} from '@angular/flex-layout';

import {AppComponent} from './app.component';
import {HomeComponent} from './home/home.component';
import {MenuComponent} from './menu/menu.component';
import {RouterModule, Routes} from '@angular/router';
import {UploadComponent} from './upload/upload.component';
import {ManageAccountComponent} from './manage-account/manage-account.component';
import {UserLibraryComponent} from './user-library/user-library.component';
import {BookBrowserComponent} from './book-browser/book-browser.component';
import {MatListModule} from '@angular/material';

const appRoutes: Routes = [
  {path: 'home', component: HomeComponent},
  {path: 'user/account', component: ManageAccountComponent},
  {path: 'upload', component: UploadComponent},
  {path: 'library', component: UserLibraryComponent},
  {path: 'books/categories', component: BookBrowserComponent},
  {path: '', redirectTo: '/home', pathMatch: 'full'}
];

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    MenuComponent,
    UploadComponent,
    ManageAccountComponent,
    UserLibraryComponent,
    BookBrowserComponent
  ],
  imports: [
    BrowserModule,
    MatToolbarModule,
    MatButtonModule,
    BrowserAnimationsModule,
    RouterModule.forRoot(appRoutes, {enableTracing: true}),
    MatListModule,
    FlexLayoutModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
