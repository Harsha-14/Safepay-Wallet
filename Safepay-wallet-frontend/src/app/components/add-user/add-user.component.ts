import { Component, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {

  signUpForm: FormGroup;
  submitted:boolean=false;
  pass:boolean;
  finish:boolean=true;
  constructor(private formBuilder: FormBuilder, private router:Router,private userService:UserService) { }

  ngOnInit() {
    if(localStorage.username!=null){
    
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
    else{
      this.router.navigate(['/list-user'])
    }
  }
  phoneNumberError:boolean=false;
  usernameError:boolean=false;
  addUser(){
    this.submitted=true;
    this.pass=false;
    this.usernameError=false;
    this.phoneNumberError=false;
    if(this.signUpForm.invalid){
      return;
    }
    if(this.signUpForm.controls.password.value!=this.signUpForm.controls.password2.value){
      this.pass=true;
      return;
    }

        this.userService.createNewUser(this.signUpForm.value).subscribe(data => {
          alert(`${this.signUpForm.controls.name.value} record is added successfully ..!`);
          this.finish=false;
          }, err => {
          if(err.error.errorMessage=="Mobile Number Already Registered"){
            this.phoneNumberError=true;
          }
          if(err.error.errorMessage=="Select Different User Name"){
            this.usernameError=true;
          }
        });
       
  }
}
