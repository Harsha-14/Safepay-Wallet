import { Component, OnInit } from '@angular/core';
import { Validators, FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { User } from 'src/app/models/user.model';

@Component({
  selector: 'app-fund-transfer',
  templateUrl: './fund-transfer.component.html',
  styleUrls: ['./fund-transfer.component.css']
})
export class FundTransferComponent implements OnInit {

  fundTransferForm: FormGroup;
  submitted: boolean = false;
  user:User;
  invalidPassword:boolean;
  finish:boolean=true;
  insufficientBalance:boolean;
  invalidAccountNumber:boolean;
  message:String;
  loading:boolean=false
  constructor(public us:UserService ,private formBuilder: FormBuilder,private router: Router) { }

  ngOnInit() {
    if (localStorage.username != null) {
      this.fundTransferForm = this.formBuilder.group({
        accountNumber:['', Validators.required],
        transfer: ['',  [Validators.required, Validators.min(1)]],
        password: ['', Validators.required]
      });
    }
    else {
      this.router.navigate(['/login']);
    }
  }

  fundTransfer(){
    this.submitted = true;
    this.invalidPassword=false;
    this.insufficientBalance=false;
    this.invalidAccountNumber=false;
    if (this.fundTransferForm.invalid) {
      return;
    }
    if(confirm('Are You Sure You Want To Transfer?'))
    {
      this.loading=true
      this.us.fundTransfer(localStorage.username,this.fundTransferForm.controls.password.value,this.fundTransferForm.controls.accountNumber.value,this.fundTransferForm.controls.transfer.value).subscribe(data => {
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

          if(err.error.errorMessage=="Password Incorrect"){
            this.invalidPassword=true;
            this.message=err.error.errorMessage
          }
          if(err.error.errorMessage=="Invalid Account Number"){
            this.invalidAccountNumber=true;
            this.message=err.error.errorMessage
          }
          if(err.error.errorMessage=="Balance Insufficient"){
            this.insufficientBalance=true;
            this.message=err.error.errorMessage
          }
        });
      }
  }

}