import { User } from './user.model';

// Creating Passbook Model class
export class Passbook{

    transactionId:String;
    action: string;
    account: User;
    amount: number;
    balance: number;
    day: String;
    time: String;
}