import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/models/user.model';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.css']
})
export class SignUpComponent implements OnInit {

  signUpForm: FormGroup;
  submitted:boolean=false;
  pass:boolean;
  finish:boolean=true;
  loading:boolean=false
  // finish:boolean=false;
  // loading:boolean=true
  accountNumb:object;
  constructor(private formBuilder: FormBuilder, private router:Router,private userService:UserService) { }

  ngOnInit() {
    this.finish=true
    if(localStorage.username!=null){
        localStorage.removeItem("username");
    }
      this.signUpForm=this.formBuilder.group({
        name: ['',[Validators.required,Validators.pattern("[A-Z][A-Z a-z]{2,14}")]],
        age: ['', [Validators.required, Validators.min(18), Validators.max(80)]],
        city: ['',Validators.required],
        phoneNumber:['',[Validators.required,Validators.pattern("[6-9][0-9]{9}")]],
        email:['',[Validators.required,Validators.email]],
        userName: ['',[Validators.required,Validators.pattern("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.*\\s).{6,10}$")]],
        password:['',[Validators.required,Validators.pattern("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.*\\s).{6,10}$")]],
        password2:['',Validators.required]
      });
  }
  phoneNumberError:boolean=false;
  userNameError:boolean=false;
  addUser(){
    this.submitted=true;
    this.pass=false;
    this.userNameError=false;
    this.phoneNumberError=false;
    if(this.signUpForm.invalid){
      return;
    }
    if(this.signUpForm.controls.password.value!=this.signUpForm.controls.password2.value){
      this.pass=true;
      return;
    }
        this.loading=true;
        this.userService.createNewUser(this.signUpForm.value).subscribe(data => {
          alert(`${this.signUpForm.controls.name.value} record is added successfully ..!`);
          this.accountNumb=data;
          console.log(this.accountNumb)
          this.loading=false;
          
            this.finish=false;
    
          }, err => {
            this.loading=false;
          if(err.error.errorMessage=="Mobile Number Already Registered"){
            this.phoneNumberError=true;
          }
          if(err.error.errorMessage=="Select Different User Name"){
            this.userNameError=true;
          }
        });
       
  }
}
