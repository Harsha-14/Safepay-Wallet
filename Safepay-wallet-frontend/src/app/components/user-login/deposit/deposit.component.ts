import { Component, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user.model';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-deposit',
  templateUrl: './deposit.component.html',
  styleUrls: ['./deposit.component.css']
})
export class DepositComponent implements OnInit {

  depositForm: FormGroup;
  submitted: boolean = false;

  constructor(public us:UserService ,private formBuilder: FormBuilder,private router: Router) { }
  user:User;
  invalidPassword:boolean=false;
  finish:boolean=true;
  message:String;
  loading:boolean=false
  ngOnInit() {
    if (localStorage.username != null) {
      this.depositForm = this.formBuilder.group({
        deposit: ['', [Validators.required, Validators.min(1)]],
        password: ['', Validators.required]
      });
    }
    else {
      this.router.navigate(['/login']);
    }
  }

  deposit(){
    this.submitted = true;
    this.invalidPassword=false;
    // login form validation
    if (this.depositForm.invalid) {
      return;
    }
    if(confirm('Are You Sure You Want To Deposit?'))
    {
      this.loading=true

      this.us.deposit(localStorage.username,this.depositForm.controls.password.value,this.depositForm.controls.deposit.value).subscribe(data => {
        // on resolve or on success
          this.finish=false;
          this.loading=false
        setTimeout(() => {
        this.router.navigate(['/userLogin/accDetails']);
      }, 3500);
      },
        err => {
          // on reject or on error
          this.loading=false
        this.invalidPassword=true;
        this.message=err.error.errorMessage
        });
      }
  }

}
