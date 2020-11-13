import { Component, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/models/user.model';

@Component({
  selector: 'app-withdraw',
  templateUrl: './withdraw.component.html',
  styleUrls: ['./withdraw.component.css']
})
export class WithdrawComponent implements OnInit {

  withdrawForm: FormGroup;
  submitted: boolean = false;
  invalidPassword:boolean=false;
  finish:boolean=true;
  insufficientBalance:boolean=false;
  message:String;
  loading:boolean=false
  constructor(public userService:UserService ,private formBuilder: FormBuilder,private router: Router) { }
  ngOnInit() {
    if (localStorage.username != null) {
      this.withdrawForm = this.formBuilder.group({
        withdraw: ['', [Validators.required, Validators.min(1)]],
        password: ['', Validators.required]
      });
    }
    else {
      this.router.navigate(['/login']);
    }
  }


  withdraw(){
    this.submitted = true;
    this.invalidPassword=false;
    this.insufficientBalance=false;
    if (this.withdrawForm.invalid) {
      return;
    }
    if(confirm('Are You Sure You Want To Withdraw?'))
    {
      this.loading=true
      this.userService.withdraw(localStorage.username,this.withdrawForm.controls.password.value,this.withdrawForm.controls.withdraw.value).subscribe(data => {
        // on resolve or on success
        this.loading=false
        this.finish=false;
        setTimeout(() => {
          this.router.navigate(['/userLogin/accDetails']);
        }, 3500);
  
      },
        err => {
          // on reject or on error
          this.loading=false
          if(err.error.errorMessage=="Password Incorrect"){
            this.invalidPassword=true;
            this.message=err.error.errorMessage
          }
          if(err.error.errorMessage=="Balance Insufficient"){
            this.insufficientBalance=true;
            this.message=err.error.errorMessage
          }
         console.log(err.stack);
        });
      }
  }

}
