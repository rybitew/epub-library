import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {

  constructor(private http: HttpClient, private router: Router) {
  }

  ngOnInit() {
  }

  private goToUser() {
    this.checkIfLoggedIn();
    this.router.navigate([`user/activity/${sessionStorage.getItem('user')}`])
  }

  private checkIfLoggedIn() {
    if (sessionStorage.getItem('authenticated') !== 'true') {
      this.router.navigate(['login']);
    }
  }

  upload(fileList: FileList) {
    this.checkIfLoggedIn();
    if (fileList.length > 0) {
      let file: File = fileList[0];
      let formData: FormData = new FormData();
      formData.append('file', file);
      this.http.post<any>('http://localhost:8082/book/upload/', formData).subscribe(
        response => console.log(response)
      );
    }
  }
}
