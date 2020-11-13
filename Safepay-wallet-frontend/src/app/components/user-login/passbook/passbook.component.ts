import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { Passbook } from 'src/app/models/passbook.model';

@Component({
  selector: 'app-passbook',
  templateUrl: './passbook.component.html',
  styleUrls: ['./passbook.component.css']
})
export class PassbookComponent implements OnInit {

  constructor(public router:Router,public us:UserService) { }
  trans:Passbook[];
  count:number;
  ngOnInit() {
    if(localStorage.username!=null){
      this.getTransactions();
    }
    else{
      this.router.navigate(['/login']);
    }
  }
  getTransactions() {
    this.us.getAllTransactions(localStorage.username).subscribe(data => {
      // on resolve or on success
      this.trans=data;
      this.count=this.trans.length;
      console.log(this.trans);
    },
      err => {
        // on reject or on error
        console.log(err.stack);
      });
  }
  // for previous and new transactions design
  limit1:number=0;
  limit2:number=5;
  old()
  {
    
    this.limit1=Math.min(this.limit1+5,this.trans.length)
    this.limit2=Math.min(this.limit2+5,this.trans.length)

  }
  new()
  {
    this.limit2=Math.max(this.limit1,0)
    this.limit1=Math.max(this.limit1-5,0)
  }
}
