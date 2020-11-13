import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-login',
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.css']
})
export class UserLoginComponent implements OnInit {

  constructor(public router:Router) { }

  ngOnInit() {
    if(localStorage.username!=null){

    }
    else{
      this.router.navigate(['/login']);
    }
  }
  openNav() {
    document.getElementById("mySidenav").style.width = "300px";
  }
  
  closeNav() {
    document.getElementById("mySidenav").style.width = "0";
  }
  logout(){
    localStorage.removeItem("username");
    this.router.navigate(["/home"]);
  }

}
