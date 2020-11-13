import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user.model';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-show-bal',
  templateUrl: './show-bal.component.html',
  styleUrls: ['./show-bal.component.css']
})
export class ShowBalComponent implements OnInit {

  balance:number;
  constructor(public router:Router,public userService:UserService) { }
  ngOnInit() {
    if(localStorage.username!=null){
      this.getUser();
    }
    else{
      this.router.navigate(['/login']);
    }
  }
  getUser() {
    this.userService.getBalance(localStorage.username).subscribe(data => {
      this.balance=data;
    },
      err => {
        console.log(err.stack);
      });
  }
}
