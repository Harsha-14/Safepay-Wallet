import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user.model';
import { UserService } from 'src/app/services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-list-user',
  templateUrl: './list-user.component.html',
  styleUrls: ['./list-user.component.css']
})
export class ListUserComponent implements OnInit {

  // searchText = "Amit";
  searchText:any;
  
  // to handle list of users
  users: User[];
  count:number
  // injecting UserService which is using HttpClient service
  constructor(private userService: UserService, private router: Router) { }

  // Life cycle hook
  ngOnInit() {
    if (localStorage.username != null) {
      this.getUsers();
    }
    else {
      this.router.navigate(['/login']);
    }
  }

  getUsers() {
    this.userService.getAllUsers().subscribe(data => {
      // on resolve or on success
      this.users = data;
      this.count=this.users.length;
      console.log(this.users);
    },
      err => {
        // on reject or on error
        // console.log("harsha");
        console.log(err.stack);
      });
  }
  // logOutUser() function
  logOutUser() {
    if (localStorage.username != null) {
      localStorage.removeItem("username");
      this.router.navigate(['/login']);
    }
  }

  // addUser() function
  addUser() {
    this.router.navigate(['/add-user']);
  }
  limit1:number=0;
  limit2:number=3;
  old()
  {
    
    this.limit1=Math.min(this.limit1+3,this.users.length)
    this.limit2=Math.min(this.limit2+3,this.users.length)

  }
  new()
  {
    this.limit2=Math.max(this.limit1,0)
    this.limit1=Math.max(this.limit1-3,0)
  }



  // deleteUser(user) function
  deleteUser(user: User) {
    let result = confirm("Do you want to delete user?");
    if (result) {
      this.userService.deleteAccount(user.userName)
        .subscribe(data => {
          this.users = this.users.filter(u => u.accountNumber !== user.accountNumber);
          this.getUsers()
        },
          err => {
            console.log(err.stack);
          });

          // alert(user.firstName+" record is deleted ..!");
          alert(`${user.name}  record is deleted ..!`);
    }
  }
}
