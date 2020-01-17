import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NavigationEnd, Router} from '@angular/router';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  public authenticated = false;
  @ViewChild('file', {static: false}) file: ElementRef;

  constructor(public http: HttpClient, public router: Router) {
    this.router.routeReuseStrategy.shouldReuseRoute = function() {
      return false;
    };

    this.router.events.subscribe((evt) => {
      if (evt instanceof NavigationEnd) {
        // trick the Router into believing it's last link wasn't previously loaded
        this.router.navigated = false;
        // if you need to scroll back to top, here is the right place
        // window.scrollTo(0, 0);
      }
    });
  }

  ngOnInit() {
    this.checkIfAuthenticated();
  }

  checkIfAuthenticated() {
    if (sessionStorage.getItem('authenticated') === 'true') {
      return true;
    } else {
      this.authenticated = false;
      return false;
    }
  }

  goToUser() {
    if (sessionStorage.getItem('authenticated') === 'true') {
      this.router.navigate(['/home']);
      this.router.navigate([`user/activity/${sessionStorage.getItem('user')}`]);
    } else {
      this.router.navigate(['login']);
    }
  }

  upload(fileList: FileList) {
    if (fileList.length > 0) {
      let file: File = fileList[0];
      let formData: FormData = new FormData();
      formData.append('file', file);
      this.http.post<any>('http://localhost:8082/book/upload/', formData).subscribe(res => {
          throw new Error('Uploaded');
        }
      );
    }
  }
}
