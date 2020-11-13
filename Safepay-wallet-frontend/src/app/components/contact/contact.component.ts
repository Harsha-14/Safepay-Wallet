import { Component, OnInit } from '@angular/core';
import { Validators, FormGroup, FormBuilder } from '@angular/forms';
import { User } from 'src/app/models/user.model';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit {

  contactForm: FormGroup;
  submitted:boolean=false;
  pass:boolean;
  res:boolean;
  user:User;
  finish:boolean=true;
  constructor(private formBuilder: FormBuilder, private router:Router,private userService:UserService) { }

  ngOnInit() {
    if(localStorage.username!=null){
        localStorage.removeItem("username");
    }
      this.contactForm=this.formBuilder.group({
        name: ['',[Validators.required,Validators.pattern("[A-Z][A-Z a-z]{2,14}")]],
        city: ['',Validators.required],
        subject:['',Validators.required],
        email:['',[Validators.required,Validators.email]],
      });
  }
  contact(){
    this.router.navigate(["/login"])
    this.finish=false;
        
  }

}
