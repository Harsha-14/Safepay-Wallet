// Creating User Model class
export class User{
    accountNumber:number;
    name: string;
    age: number;
    city:string;
    phoneNumber: string;
    email: string;
    balance:number;
    userName:string;
    password: string;
}

export class LoggingService{
    logStatus(message:String){
        let currentDateTime=new Date();
        let currentDateTimeString=currentDateTime.toString();
        console.log(` ${(currentDateTimeString)} : `,message);
        
    }
}