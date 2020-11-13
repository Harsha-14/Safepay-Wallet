package com.cg.safepaywallet.dao;

import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Repository;
import com.cg.safepaywallet.bean.Account;
import com.cg.safepaywallet.bean.Passbook;
import com.cg.safepaywallet.exceptions.AccountNumberDoesNotExistsException;
import com.cg.safepaywallet.exceptions.InsufficientBalanceException;
import com.cg.safepaywallet.exceptions.InvalidLoginCredentialsException;
import com.cg.safepaywallet.exceptions.MobileNumberRegisteredException;
import com.cg.safepaywallet.exceptions.ResourceNotFoundException;
import com.cg.safepaywallet.exceptions.UserNameExistsException;
@Repository
public class WalletDAOImpl implements WalletDao{

	@Autowired
	EntityManager entityManager;
	Logger logger=LoggerFactory.getLogger(WalletDAOImpl.class);

	@Autowired
	private JavaMailSender javaMailSender;
	
//	boolean sendMail=true;
	boolean sendMail=false;
	
	SimpleMailMessage mail = new SimpleMailMessage();
	
	/*
 * Method: getAll
 * Description: Used to get all account details
 * @return list: It returns a list
 */		
	
		@Transactional
		@Override
		public List<Account> getAll(){
			String qStr = "SELECT a from Account a";
			TypedQuery<Account> query = entityManager.createQuery(qStr, Account.class);
			List<Account> list=query.getResultList();
			return list;
			
		}
		
	/*
	 * Method: getAccount 
	 * Description: Used to get account based on username
	 * @param userName: UserName of user account
	 * @return account: It return a user account on success
	 * @throws ResourceNotFoundException : It is raised if User doesnot exists with given username
	 * 
	 */		
		
	@Transactional
	@Override
	public Account getAccount(String userName) throws ResourceNotFoundException{
		 
		Account account=null;
		String Str = "SELECT a.userName FROM Account a";
		TypedQuery<String> query = entityManager.createQuery(Str, String.class);
		List<String> list= query.getResultList();
		if(list.contains(userName)) {
			Str = "SELECT a FROM Account a where userName=:username";
			TypedQuery<Account> query1 = entityManager.createQuery(Str, Account.class);
			query1.setParameter("username", userName);
			account= query1.getSingleResult();
		}
		else {
			logger.error("User Doesnot Exists With given UserName");
			throw new ResourceNotFoundException("User Does Not Exists");
		}
		return account;
	}	

	/*
	 * Method: addAccount 
	 * Description: Used for user registration /sign up 
	 * @param account: account details
	 * @return int: returns account number generated after registration
	 * 
	 */
	
	@Transactional
	@Override
	public int addAccount(Account account) {

		entityManager.persist(account);
		if(sendMail)
		registrationMail(account);
		return account.getAccountNumber();

	
	}
	
	/*
	 * Method: editAccount 
	 * Description: Used to edit details of the account
	 * @param account: Edited account details
	 */
	
	@Transactional
	@Override
	public void editAccount(Account account) {
		String Str = "SELECT a FROM Account a where userName=:uname";
		TypedQuery<Account> query = entityManager.createQuery(Str, Account.class);
		query.setParameter("uname", account.getUserName());
		Account account2= query.getResultList().get(0);
		account.setBalance(account2.getBalance());
		account.setAccountNumber(account2.getAccountNumber());
		account.setName(account2.getName());
		account.setPhoneNumber(account2.getPhoneNumber());
		entityManager.merge(account);
//		if(sendMail)
//		editProfileMail(account);
	}


	/*
	 * Method: deleteAccount 
	 * Description: Used to delete Account of the user from database 
	 * @param userName: UserName of user account
	 */
	
	@Transactional
	@Override
	public void deleteAccount(String userName) throws ResourceNotFoundException {
		Account account=getAccount(userName);
		entityManager.remove(account);
		if(sendMail)
		deleteProfileMail(account);

	}


	/*
	 * Method: showBalance 
	 * Description: Used to get balance amount in user Account
	 * @param userName: UserName of user account
	 * @return double: It return a double on success
	 */

	@Transactional
	@Override
	public double showBalance(String userName) throws ResourceNotFoundException {
		
			Account account=getAccount(userName);
			return account.getBalance();
	}
	
	/*
	 * Method: deposit 
	 * Description: Used to deposit amount into user Account
	 * @param userName: UserName of user account
	 * @param password: Password of user account
	 * @param amount: Amount being deposited by the user
	 * @return double: It return a double on success
	 */
	
	@Transactional
	@Override
	public double deposit(String userName,String password,double amount) throws ResourceNotFoundException, InvalidLoginCredentialsException {
		loginValidate(userName,password);
		Account account=getAccount(userName);
		double bal=account.getBalance()+amount;
		account.setBalance(bal);
		Passbook passbook=new Passbook(generateTransactionId(),"Credit",amount,account.getBalance());
		account.addTransaction(passbook);
		entityManager.merge(account);
		if(sendMail)
		depositMail(account, passbook);
		return amount;		 
	}

	
	/*
	 * Method: withdraw 
	 * Description: Used to withdraw amount from user Account
	 * @param userName: UserName of user account
	 * @param password: Password of user account
	 * @param amount: Amount being transferred by the user
	 * @return double: It return a double on success
	 * @throws InsufficientBalanceException : It is raised if User doesnot have
	 * Sufficient Amount to transfer
	 */
	
	@Transactional
	@Override
	public double withdraw(String userName,String password,double amount) throws ResourceNotFoundException, InsufficientBalanceException, InvalidLoginCredentialsException {
		loginValidate(userName,password);
		Account account=getAccount(userName);
		double bal=account.getBalance()-amount;
		if(bal>=0.0) {
			account.setBalance(bal);
			Passbook passbook=new Passbook(generateTransactionId(),"Debit",amount,account.getBalance());
			account.addTransaction(passbook);

			entityManager.merge(account);
			if(sendMail)
			withdrawMail(account, passbook);
			return amount;
		}
		else {
			logger.error("Insufficient Balance for Withdraw");
			throw new InsufficientBalanceException("Balance Insufficient");	
		}
	}

	/*
	 * Method: fundTransfer 
	 * Description: Used to transfer amount from one Account to other Account
	 * @param userName: UserName of user account
	 * @param password: Password of user account
	 * @param amount: Amount being transferred by the user
	 * @param recepientAccountNumber: Account Number of the Recepient
	 * @return double: It return a double on success
	 * @throws AccountNumberDoesnotExistsException : It is raised if Recepient Account Number is not Valid
	 * @throws InsufficientBalanceException : It is raised if User doesnot have Sufficient Amount to transfer
	 */
	
	@Transactional
	@Override
	public double fundTransfer(String userName,String password,int recepientAccountNumber, double amount) throws ResourceNotFoundException, AccountNumberDoesNotExistsException, InsufficientBalanceException, InvalidLoginCredentialsException {
		loginValidate(userName,password);
		Account account=getAccount(userName);
		double bal=account.getBalance()-amount;
		if(bal>=0) 
		{
			
			if(TransferAccountExists(recepientAccountNumber)) 
			{
				account.setBalance(bal);
	
				String command = "SELECT a FROM Account a WHERE a.accountNumber=:user";
				TypedQuery<Account> query = entityManager.createQuery(command, Account.class);
				query.setParameter("user", recepientAccountNumber);
	
				Account account2=query.getSingleResult();
				if(account2!=null) 
				{
					account.setBalance(bal);
					account2.setBalance(account2.getBalance()+amount);
					Passbook passbook1=new Passbook(generateTransactionId(),"Transfer to "+account2.getAccountNumber(),amount,account.getBalance());
					account.addTransaction(passbook1);
					entityManager.merge(account);
					Passbook passbook2=new Passbook(generateTransactionId(),"Credit from "+account.getAccountNumber(),amount,account2.getBalance());
					account2.addTransaction(passbook2);
					entityManager.merge(account2);
					if(sendMail)
					fundTransferrerMail(account, account2, passbook1);
					if(sendMail)
					fundRecievererMail(account, account2, passbook2);
				}
			}
			else 
			{
				logger.error("Invalid Account Number");
				throw new AccountNumberDoesNotExistsException("Invalid Account Number");
			}

			System.err.println(amount);
			return amount;
		}
		else 
		{
			logger.error("Insufficient Balance for Transfer");
			throw new InsufficientBalanceException("Balance Insufficient");
		}					
	}
	/*
	 * Method: transferAccountExisist 
	 * Description: Used to check wether recepient account number exists or not
	 * @param recepientAccountNumber: Account Number of the Recepient
	 * @return boolean: It return true if account number exists and false if does not exists
	 */
	
	@Transactional
	@Override
	public boolean TransferAccountExists(int recepientAccountNumber) {

		String command = "SELECT a.accountNumber FROM Account a";
		TypedQuery<Integer> query = entityManager.createQuery(command, Integer.class);
		List<Integer> list=query.getResultList();
		if(list.contains(recepientAccountNumber)) {
			return true;
		}
		else 
			return false;
		
	}

	/*
	 * Method: getAllTransactions 
	 * Description: Used to get all the transactions done by the user
	 * @param userName: UserName of user account
	 * @return list: It returns a list of transactions on success
	 */
	
	@Transactional
	@Override
	public List<Passbook> getAllTransactions(String userName) throws ResourceNotFoundException {
		Account account=getAccount(userName);
		List<Passbook> list=account.getTransactions();
		Collections.sort(list);
		Collections.reverse(list);
		return list;
		
	}

	/*
	 * Method: generateTransactionId 
	 * Description: Used to generate transaction id for every transaction
	 * @return String: It return a string of new transaction id
	 */
	@Transactional
	@Override
	public String generateTransactionId() {

		String qStr = "SELECT max(a.transactionId) from Passbook a";
		TypedQuery<String> query = entityManager.createQuery(qStr, String.class);
		String existingTransactionId=query.getSingleResult();
		if(existingTransactionId!=null) {
			String transactionId="TN"+String.format("%04d", Integer.parseInt(existingTransactionId.substring(2))+1);
			return transactionId;
		}
		else
			return "TN0001";
	}

	/*
	 * Method: loginValidate 
	 * Description: Used to transfer amount from one Account to other Account
	 * @param userName: UserName of user account
	 * @param password: Password of user account
	 * @return boolean: It return true on success
	 * @throws InvalidLoginCredentialsException : It is raised if Login Credentials are not Valid
	 */
	
	@Transactional
	@Override
	public boolean loginValidate(String userName, String password) throws ResourceNotFoundException, InvalidLoginCredentialsException {
		Account account;
		String command = "SELECT a.userName FROM Account a";
		TypedQuery<String> query = entityManager.createQuery(command, String.class);
		List<String> list=query.getResultList();
		if(list.contains(userName)) {
			account=getAccount(userName);
			if(account.getPassword().equals(password)) {
				return true;
			}
		}
			logger.error("Incorrect Password");
			throw new InvalidLoginCredentialsException("Password Incorrect");
	}

	/*
	 * Method: AccountExists 
	 * Description: Used to check wether the user is already registered or not during Account Creation
	 * @param userName: UserName of new user account
	 * @param phoneNumber: phoneNumber of new user account
	 * @return int: It return 0 on success
	 * @throws MobileNumberRegisteredException : It is raised if phone number already exists
	 * @throws UserNameExistsException : It is raised if username already exists
	 */	
	
	@Transactional
	@Override
	public int AccountExists(String userName,String phoneNumber) throws MobileNumberRegisteredException,UserNameExistsException{
		String command = "SELECT a.phoneNumber FROM Account a";
		TypedQuery<String> query = entityManager.createQuery(command, String.class);
		List<String> list=query.getResultList();
		if(list.contains(phoneNumber)) {
			logger.error("Phone Number Already Registered");
			throw new MobileNumberRegisteredException("Mobile Number Already Registered");
		}
		command = "SELECT a.userName FROM Account a";
		query = entityManager.createQuery(command, String.class);
		List<String> list1=query.getResultList();
		if(list1.contains(userName)) {
			logger.error("UserName Already Exists");
			throw new UserNameExistsException("Select Different User Name");
		}
		return 0;
	}
	
	/*
	 * Method: generateAccountNumber 
	 * Description: Used to generate AccountNumber during registration of user
	 * @return int: It return a new account number
	 */
	
	@Transactional
	@Override
	public int generateAccountNumber() {
		String qStr = "SELECT count(a.accountNumber) from Account a";
		TypedQuery<Long> query = entityManager.createQuery(qStr, Long.class);
		long count=query.getSingleResult();
		if(count!=0) {
			String qStr1 = "SELECT a.accountNumber FROM Account a where a.accountNumber=(SELECT MAX(a.accountNumber) FROM Account a)";
			TypedQuery<Integer> query1 = entityManager.createQuery(qStr1, Integer.class);
			int accountNumber=query1.getSingleResult();
			return ++accountNumber;
		}
		else
			return 1111;
	
	}
	public void registrationMail(Account account) {
		mail.setTo(account.getEmail());
		mail.setSubject("Welcome to Safepay");

		mail.setText("Hello "+account.getName()+",\r\n" + 
				"\r\n" + 
				"Thank you so much for allowing us to help you with your recent account opening. We are committed to providing our customers with the highest level of service and the most innovative banking products possible.\r\n" + 
				"\r\n" + 
				"We are very glad you chose us as your financial institution and hope you will take advantage of our wide variety of savings all designed to meet your specific needs.\r\n" + 
				"\r\n" + 
				"Account Number\t:\t"+account.getAccountNumber()+"\r\n" + 
				"\r\n" + 
				"For more detailed information about any of our services, please refer to our website, www.safepaybank.com, or visit any of our convenient locations. You may contact us by phone at 9492056849.\r\n" + 
				"\r\n" + 
				"Please do not hesitate to contact me, if you have any questions. We will contact you in the very near future to ensure you are completely satisfied with the services you have received thus far.\r\n" + 
				"\r\n" + 
				"Respectfully,\r\n" + 
				"\r\n" + 
				"S.Sri Harsha\r\n" + 
				"Managing Director");
		javaMailSender.send(mail);		
	}
	public void editProfileMail(Account account) {
		mail.setTo(account.getEmail());
		mail.setSubject("Profile Update");

		mail.setText("Hello "+account.getName()+",\r\n" + 
				"\r\n" + 
				"This is a confirmation that your profile has been updated successfully.\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"For more detailed information about any of our services, please refer to our website, www.safepaybank.com, or visit any of our convenient locations. You may contact us by phone at 9492056849.\r\n" + 
				"\r\n" + 
				"Please do not hesitate to contact me, if you have any questions. We will contact you in the very near future to ensure you are completely satisfied with the services you have received thus far.\r\n" + 
				"Respectfully,\r\n" + 
				"\r\n" + 
				"S.Sri Harsha\r\n" + 
				"Managing Director");
		javaMailSender.send(mail);
	}
	public void deleteProfileMail(Account account) {
		mail.setTo(account.getEmail());
		mail.setSubject("Account Deletion");

		mail.setText("Hello "+account.getName()+",\r\n" + 
				"\r\n" + 
				"This is a confirmation that your Account has been Deleted.\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"For more detailed information about any of our services, please refer to our website, www.safepaybank.com, or visit any of our convenient locations. You may contact us by phone at 9492056849.\r\n" + 
				"\r\n" + 
				"Please do not hesitate to contact me, if you have any questions. We will contact you in the very near future to ensure you are completely satisfied with the services you have received thus far.\r\n" + 
				"Respectfully,\r\n" + 
				"\r\n" + 
				"S.Sri Harsha\r\n" + 
				"Managing Director");
		javaMailSender.send(mail);
	}
	public void depositMail(Account account,Passbook passbook) {
		mail.setTo(account.getEmail());
		mail.setSubject("Deposit");

		mail.setText("Hello "+account.getName()+",\r\n" + 
				"\r\n" + 
				"Thank you for the recent transaction that you made on "+passbook.getDay()+"for the amount of "+passbook.getAmount()+"/-\r\n" + 
				"\r\n" + 
				"This is a confirmation that amount has been successfully recieved and deposited in your account"+"\r\n" + 
				"\r\n" + 
				"\tTRANSACTION DETAILS\t\r\n" + 
				"\r\n" + 
				"TRANSACTION ID		: "+passbook.getTransactionId()+"\r\n" + 
				"\r\n" + 
				"TRANSACTION DATE	: "+passbook.getDay()+"\r\n" + 
				"\r\n" + 
				"TRANSACTION TIME	: "+passbook.getTime()+"\r\n" + 
				"\r\n" + 
				"TRANSACTION TYPE	: "+passbook.getAction()+"\r\n" + 
				"\r\n" + 
				"TRANSACTION AMOUNT	: "+passbook.getAmount()+"/-\r\n" +
				"\r\n" + 
				"UPDATED BALANCE	: "+passbook.getBalance()+"/-\r\n" + 
				"\r\n" + 
				"For more detailed information about any of our services, please refer to our website, www.safepaybank.com, or visit any of our convenient locations. You may contact us by phone at 9492056849.\r\n" + 
				"\r\n" + 
				"Please do not hesitate to contact me, if you have any questions. We will contact you in the very near future to ensure you are completely satisfied with the services you have received thus far.\r\n" + 
				"Respectfully,\r\n" + 
				"\r\n" + 
				"S.Sri Harsha\r\n" + 
				"Managing Director");
		javaMailSender.send(mail);

	}
	public void withdrawMail(Account account, Passbook passbook) {
		mail.setTo(account.getEmail());
		mail.setSubject("Withdraw");

		mail.setText("Hello "+account.getName()+",\r\n" + 
				"\r\n" + 
				"Thank you for the recent transaction that you made on "+passbook.getDay()+"for the amount of "+passbook.getAmount()+"/-\r\n" + 
				"\r\n" + 
				"This is a confirmation that amount has been successfully withdrawn your account."+"\r\n" + 
				"\r\n" + 
				"\tTRANSACTION DETAILS\t\r\n" + 
				"\r\n" + 
				"TRANSACTION ID		: "+passbook.getTransactionId()+"\r\n" + 
				"\r\n" + 
				"TRANSACTION DATE	: "+passbook.getDay()+"\r\n" + 
				"\r\n" + 
				"TRANSACTION TIME	: "+passbook.getTime()+"\r\n" + 
				"\r\n" + 
				"TRANSACTION TYPE	: "+passbook.getAction()+"\r\n" + 
				"\r\n" + 
				"TRANSACTION AMOUNT	: "+passbook.getAmount()+"/-\r\n" +
				"\r\n" + 
				"UPDATED BALANCE	: "+passbook.getBalance()+"/-\r\n" + 
				"\r\n" + 
				"For more detailed information about any of our services, please refer to our website, www.safepaybank.com, or visit any of our convenient locations. You may contact us by phone at 9492056849.\r\n" + 
				"\r\n" + 
				"Please do not hesitate to contact me, if you have any questions. We will contact you in the very near future to ensure you are completely satisfied with the services you have received thus far.\r\n" + 
				"Respectfully,\r\n" + 
				"\r\n" + 
				"S.Sri Harsha\r\n" + 
				"Managing Director");
		javaMailSender.send(mail);
		
	}

	public void fundTransferrerMail(Account account,Account account2,Passbook passbook1) {
		mail.setTo(account.getEmail());
		mail.setSubject("Fund Transfer");
		mail.setText("Hello "+account.getName()+",\r\n" + 
				"\r\n" + 
				"Thank you for the recent transaction that you made on "+passbook1.getDay()+"for the amount of "+passbook1.getAmount()+"/-\r\n" + 
				"\r\n" + 
				"This is a confirmation that amount has been successfully Transferred from your account to account number "+account2.getAccountNumber()+"."+"\r\n" + 
				"\r\n" + 
				"\tTRANSACTION DETAILS\t\r\n" + 
				"\r\n" + 
				"TRANSACTION ID		: "+passbook1.getTransactionId()+"\r\n" + 
				"\r\n" + 
				"TRANSACTION DATE	: "+passbook1.getDay()+"\r\n" + 
				"\r\n" + 
				"TRANSACTION TIME	: "+passbook1.getTime()+"\r\n" + 
				"\r\n" + 
				"TRANSACTION TYPE	: "+"Fund Transfer"+"\r\n" + 
				"\r\n" + 
				"TRANSACTION AMOUNT	: "+passbook1.getAmount()+"/-\r\n" +
				"\r\n" + 							
				"UPDATED BALANCE	: "+passbook1.getBalance()+"/-\r\n" + 
				"\r\n" + 
				"RECEPIENT ACCOUNT NUMBER	: "+account2.getAccountNumber()+"\r\n" + 
				"\r\n" + 
				"For more detailed information about any of our services, please refer to our website, www.safepaybank.com, or visit any of our convenient locations. You may contact us by phone at 9492056849.\r\n" + 
				"\r\n" + 
				"Please do not hesitate to contact me, if you have any questions. We will contact you in the very near future to ensure you are completely satisfied with the services you have received thus far.\r\n" + 
				"Respectfully,\r\n" + 
				"\r\n" + 
				"S.Sri Harsha\r\n" + 
				"Managing Director");
		javaMailSender.send(mail);
		
	}
	public void fundRecievererMail(Account account,Account account2,Passbook passbook2) {
		mail.setTo(account2.getEmail());
		mail.setSubject("Fund Transfer");
		mail.setText("Hello "+account2.getName()+",\r\n" + 
				"\r\n" + 
				"Thank you for the recent transaction that you made on "+passbook2.getDay()+"for the amount of "+passbook2.getAmount()+"/-\r\n" + 
				"\r\n" + 
				"This is a confirmation that amount has been successfully Transferred to your account from account number"+account.getAccountNumber()+"."+"\r\n" + 
				"\r\n" + 
				"\tTRANSACTION DETAILS\t\r\n" + 
				"\r\n" + 
				"TRANSACTION ID		: "+passbook2.getTransactionId()+"\r\n" + 
				"\r\n" + 
				"TRANSACTION DATE	: "+passbook2.getDay()+"\r\n" + 
				"\r\n" + 
				"TRANSACTION TIME	: "+passbook2.getTime()+"\r\n" + 
				"\r\n" + 
				"TRANSACTION TYPE	: "+"Credit"+"\r\n" + 
				"\r\n" + 
				"TRANSACTION AMOUNT	: "+passbook2.getAmount()+"/-\r\n" +
				"\r\n" + 							
				"UPDATED BALANCE	: "+passbook2.getBalance()+"/-\r\n" + 
				"\r\n" + 
				"TRANSFERRED FROM ACCOUNT NUMBER	: "+account.getAccountNumber()+"\r\n" + 
				"\r\n" + 
				"For more detailed information about any of our services, please refer to our website, www.safepaybank.com, or visit any of our convenient locations. You may contact us by phone at 9492056849.\r\n" + 
				"\r\n" + 
				"Please do not hesitate to contact me, if you have any questions. We will contact you in the very near future to ensure you are completely satisfied with the services you have received thus far.\r\n" + 
				"Respectfully,\r\n" + 
				"\r\n" + 
				"S.Sri Harsha\r\n" + 
				"Managing Director");
		javaMailSender.send(mail);
		
	}
	


}
