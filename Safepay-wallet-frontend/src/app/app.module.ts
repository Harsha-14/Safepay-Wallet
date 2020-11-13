import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {ReactiveFormsModule, FormsModule} from '@angular/forms'
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { UserLoginComponent } from './components/user-login/user-login.component';
import { SignUpComponent } from './components/sign-up/sign-up.component';
import { AccDetailsComponent } from './components/user-login/acc-details/acc-details.component';
import { DepositComponent } from './components/user-login/deposit/deposit.component';
import { WithdrawComponent } from './components/user-login/withdraw/withdraw.component';
import { PassbookComponent } from './components/user-login/passbook/passbook.component';
import { FundTransferComponent } from './components/user-login/fund-transfer/fund-transfer.component';
import { LoginComponent } from './components/login/login.component';
import { HttpClientModule } from '@angular/common/http';
import { ListUserComponent } from './components/list-user/list-user.component';
import { EditUserComponent } from './components/edit-user/edit-user.component';
import { AddUserComponent } from './components/add-user/add-user.component';
import { AboutComponent } from './components/about/about.component';
import { ContactComponent } from './components/contact/contact.component';
import { ShowBalComponent } from './components/user-login/show-bal/show-bal.component';
import { SearchPipe } from './pipes/search.pipe';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    UserLoginComponent,
    SignUpComponent,
    AccDetailsComponent,
    DepositComponent,
    WithdrawComponent,
    PassbookComponent,
    FundTransferComponent,
    LoginComponent,
    ListUserComponent,
    EditUserComponent,
    AddUserComponent,
    AboutComponent,
    ContactComponent,
    ShowBalComponent,
    SearchPipe
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule, 
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
