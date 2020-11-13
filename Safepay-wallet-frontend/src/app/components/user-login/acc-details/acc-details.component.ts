import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/models/user.model';

@Component({
  selector: 'app-acc-details',
  templateUrl: './acc-details.component.html',
  styleUrls: ['./acc-details.component.css']
})
export class AccDetailsComponent implements OnInit {

  constructor(public router:Router,public us:UserService) { }
  user:User;
  accountNumber:number;
  name:String;
  city:String;
  age:number;
  phoneNumber:String;
  mailId:String;

  ngOnInit() {
    if(localStorage.username!=null){
      this.getUser();
    }
    else{
      this.router.navigate(['/login']);
    }
  }
  getUser() {
    this.us.getUserByUserName(localStorage.username).subscribe(data => {
      // on resolve or on success
      this.user = data;
      console.log(this.user);
      this.accountNumber=this.user.accountNumber;
      this.city=this.user.city;
      this.age=this.user.age;
      this.phoneNumber=this.user.phoneNumber;
      this.mailId=this.user.email;
      this.name=this.user.name;
    },
      err => {
        // on reject or on error
        console.log(err.stack);
      });

  }

}
