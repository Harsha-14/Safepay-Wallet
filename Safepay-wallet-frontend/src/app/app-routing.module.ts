import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { AccDetailsComponent } from './components/user-login/acc-details/acc-details.component';
import { DepositComponent } from './components/user-login/deposit/deposit.component';
import { WithdrawComponent } from './components/user-login/withdraw/withdraw.component';
import { FundTransferComponent } from './components/user-login/fund-transfer/fund-transfer.component';
import { PassbookComponent } from './components/user-login/passbook/passbook.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { LoginComponent } from './components/login/login.component';
import { UserLoginComponent } from './components/user-login/user-login.component';
import { ListUserComponent } from './components/list-user/list-user.component';
import { AddUserComponent } from './components/add-user/add-user.component';
import { EditUserComponent } from './components/edit-user/edit-user.component';
import { ShowBalComponent } from './components/user-login/show-bal/show-bal.component';
import { AboutComponent } from './components/about/about.component';
import { ContactComponent } from './components/contact/contact.component';


const routes: Routes = [
  {path:'',component:HomeComponent},
  {path:'home',component:HomeComponent},
  {path:'login',component:LoginComponent},
  {path:'signUp',component:SignUpComponent},
  {path:'about',component:AboutComponent},
  {path:'list-user',component:ListUserComponent},
  {path:'add-user',component:AddUserComponent},
  {path:'contact',component:ContactComponent},
  {path:'userLogin',component:UserLoginComponent,
  children: [ 
              {path: '',component: AccDetailsComponent},
              {path: 'accDetails',component: AccDetailsComponent},
              {path:'balance',component:ShowBalComponent},
              {path: 'deposit',component: DepositComponent},
              {path: 'withdraw',component: WithdrawComponent},
              {path: 'fundTransfer',component: FundTransferComponent},
              {path: 'passbook',component: PassbookComponent},
              {path:'edit-user',component:EditUserComponent},
              {path: '**',component: AccDetailsComponent},
            ]},
  {path:'**',component:HomeComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
// {path: '',component: AccDetailsComponent},

export class AppRoutingModule { }
