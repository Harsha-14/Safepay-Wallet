import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { User, LoggingService } from 'src/app/models/user.model';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers:[LoggingService]
})
export class LoginComponent implements OnInit {

  loginForm:FormGroup;
  submitted:boolean=false;
  user:User;
  res:boolean;
  constructor(private formBuilder: FormBuilder,public router:Router,public us:UserService,public loggingService:LoggingService) { }

  ngOnInit() {
    if(localStorage.username!=null){
      localStorage.removeItem("username");
  }

    this.loginForm=this.formBuilder.group({
      userName:['',Validators.required],
      password: ['',Validators.required]
    });
  }
  verifyLogin(){
    this.submitted=true;
    if(this.loginForm.invalid){
      return;
    }
    let username=this.loginForm.controls.userName.value;
    let password=this.loginForm.controls.password.value;
    if(username == "admin" && password=="123")
    {
      localStorage.username=username;
      sessionStorage.username=username;
      this.loggingService.logStatus(localStorage.username+" login successful")
      this.router.navigate(['/list-user']);
    }

    this.us.loginValidate(this.loginForm.controls.userName.value,this.loginForm.controls.password.value).subscribe(data => {
      //on Success 
      localStorage.username=username;
        sessionStorage.username=username;
        this.loggingService.logStatus(localStorage.username+" login successful")
        this.router.navigate(['/userLogin']);
    },
      err => {
        // on reject or on error
        this.invalidLogin=true;
      });
    }
    invalidLogin:boolean=false;
    signUp(){
      this.router.navigate(['/signUp']);
    }
  
}