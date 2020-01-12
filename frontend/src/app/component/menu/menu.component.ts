import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  private authenticated = false;
  @ViewChild('file', {static: false}) file: ElementRef;

  constructor(private http: HttpClient, private router: Router) {
  }

  ngOnInit() {
    this.checkIfAuthenticated();
  }

  checkIfAuthenticated() {
    if (sessionStorage.getItem('authenticated') === 'true') {
      // this.file.click();
    } else {
      this.authenticated = false;
    }
  }

  goToUser() {
    if (sessionStorage.getItem('authenticated') === 'true') {
      this.router.navigate([`user/activity/${sessionStorage.getItem('user')}`]);
    } else {
      this.router.navigate(['login']);
    }
  }

  upload(fileList: FileList) {
    if (sessionStorage.getItem('authenticated') === 'true') {
      if (fileList.length > 0) {
        let file: File = fileList[0];
        let formData: FormData = new FormData();
        formData.append('file', file);
        this.http.post<any>('http://localhost:8082/book/upload/', formData).subscribe(
          response => console.log(response)
        );
      }
    } else {
      this.router.navigate(['login']);
    }
  }
}
