import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../models/user.model';
import { Passbook } from '../models/passbook.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  // Injecting HttpClient service to make REST Api calls.
  // To perform CRUD operations with Http Verbs - GET, POST, PUT, DELETE
  constructor(private http: HttpClient) { }

  // endpoint or REST api URL
  baseUrl: string = "http://localhost:8081/abc";

  // GET - GETS ALL USERS DETAILS
  getAllUsers() {
    return this.http.get<User[]>(this.baseUrl+"/allUsers");
  }

  // GET - GETS ALL TRANSACTIONS DONE BY THE USER
  getAllTransactions(userName:String) {
    return this.http.get<Passbook[]>(this.baseUrl+"/passbook/"+userName);
  }

  getBalance(userName:String){
    return this.http.get<number>(this.baseUrl+"/balance/"+userName);
  }


  // GET - GET USER DETAILS OF SINGLE USER
  getUserByUserName(userName: String) {
      return this.http.get<User>(this.baseUrl + "/user/" + userName);
  }

  // GET - CHECKS WETHER USERNAME AND PASSWORD MATCHES DURING LOGIN
  loginValidate(userName: String,password: String) {
      return this.http.get<boolean>(this.baseUrl + "/pass/" + userName+ "/" +password);
  }

  // GET - DEPOSIT AMOUNT
  deposit(userName: String,password:String,deposit:number) {
    return this.http.get<number>(this.baseUrl + "/deposit/" + userName+ "/" + password+ "/" +deposit);
  }
  
  // GET - WITHDRAW AMOUNT
  withdraw(userName: String,password:String,withdraw:number) {
    return this.http.get<number>(this.baseUrl + "/withdraw/" + userName+ "/" + password+ "/" +withdraw);
  }

  // GET - TRANSFER OF AMOUNT 
  fundTransfer(userName: String,password:String,accNum2:number,transfer:number) {
    return this.http.get<number>(this.baseUrl + "/ft/" + userName+ "/" + password+ "/" +accNum2+ "/" +transfer);
  }
  

  // POST - CREATING NEW ACCOUNT
  createNewUser(user: User) {
    // console.log(user)
    return this.http.post(this.baseUrl+"/add", user);
  }

  // PUT - update User
  editAccount(user: User) {
    return this.http.put(this.baseUrl + "/edit/" + user.userName, user);
  }

  // DELETE - delete User
  deleteAccount(userName:String) {
    return this.http.delete(this.baseUrl + "/delete/" + userName);
  }
}
