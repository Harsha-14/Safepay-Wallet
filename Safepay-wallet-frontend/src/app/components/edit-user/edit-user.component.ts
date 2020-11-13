import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { UserService } from 'src/app/services/user.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent implements OnInit {

  editForm: FormGroup;
  submitted: boolean = false;
  finish:boolean=true;
  loading:boolean=false
  currentId: number;
  // Injecting all relevant services inside the constructor
  constructor(private formBuilder: FormBuilder,private router: Router, private userService: UserService,private route: ActivatedRoute) {
  }

  ngOnInit() {
    if (localStorage.username != null) {
      this.editForm = this.formBuilder.group({
        accountNumber: [{ value: '', disabled: true }, Validators.required],
        name: [{ value: '', disabled: true },[Validators.required,Validators.pattern("[A-Z][A-Z a-z]{2,14}")]],
        age: ['', [Validators.required, Validators.min(18), Validators.max(80)]],
        city: ['',Validators.required],
        phoneNumber:[{ value: '', disabled: true },[Validators.required,Validators.pattern("[6-9][0-9]{9}")]],
        email:['',[Validators.required,Validators.email]],
        balance:[],
        userName: [{ value: '', disabled: true },Validators.required],
        password:['',[Validators.required,Validators.pattern("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.*\\s).{6,10}$")]],
      });

      // pulling data from service by id
      this.userService.getUserByUserName(localStorage.username).subscribe(data => {
        console.log(data);
        // binding josn response to form object
        this.editForm.setValue(data);
      },
        err => {
          console.log(err.stack);
        })
    }
    else {
      this.router.navigate(['/login']);
    }
  }
  

  // logOutUser() function
  logOutUser() {
    if (localStorage.username != null) {
      localStorage.removeItem("username");
      this.router.navigate(['/login']);
    }
  }
  // updateUser() function
  updateUser() {
    this.submitted = true;
    if (this.editForm.invalid) {
      return;
    }
  this.loading=true
    this.userService.editAccount(this.editForm.getRawValue()).subscribe(data => {
      this.finish=false;
      this.loading=false
    setTimeout(() => {
    this.router.navigate(['/userLogin/accDetails']);
  }, 3500);
},
      err => {
        this.loading=false
        console.log(err.stack);
      })
  }
}
