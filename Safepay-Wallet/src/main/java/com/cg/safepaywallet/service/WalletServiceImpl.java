package com.cg.safepaywallet.service;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.safepaywallet.bean.Account;
import com.cg.safepaywallet.bean.Passbook;
import com.cg.safepaywallet.dao.WalletDAOImpl;
import com.cg.safepaywallet.exceptions.AccountNumberDoesNotExistsException;
import com.cg.safepaywallet.exceptions.InsufficientBalanceException;
import com.cg.safepaywallet.exceptions.InvalidLoginCredentialsException;
import com.cg.safepaywallet.exceptions.MobileNumberRegisteredException;
import com.cg.safepaywallet.exceptions.ResourceNotFoundException;
import com.cg.safepaywallet.exceptions.UserNameExistsException;
@Service
public class WalletServiceImpl implements WalletService{
	
	@Autowired
	WalletDAOImpl walletDao=new WalletDAOImpl();
	String s;
	int t;
	public WalletServiceImpl(){
		
	}
	
/*
 * Method: getAll
 * Description: Used to get all account details
 * @return list: It returns a list
 */		
	
	@Override
	public List<Account> getAll() {
		return walletDao.getAll();
	}
	
/*
 * Method: getAccount 
 * Description: Used to get account based on username
 * @param userName: UserName of user account
 * @return account: It return a user account on success
 * @throws ResourceNotFoundException : It is raised if User doesnot exists with given username
 * 
 */		
	
	@Override
	public Account getAccount(String userName) throws ResourceNotFoundException {
		return walletDao.getAccount(userName);
	}

	/*
	 * Method: addAccount 
	 * Description: Used for user registration /sign up 
	 * @param account: account details
	 * @return int: returns account number generated after registration
	 * 
	 */
	
	@Override
	public int addAccount(Account account) throws MobileNumberRegisteredException, UserNameExistsException {
		walletDao.AccountExists(account.getUserName(), account.getPhoneNumber());
		account.setAccountNumber(walletDao.generateAccountNumber());
		return walletDao.addAccount(account);
	}

	/*
	 * Method: showBalance 
	 * Description: Used to get balance amount in user Account
	 * @param userName: UserName of user account
	 * @return double: It return a double on success
	 */

	@Override
	public double showBalance(String userName) throws ResourceNotFoundException {

		return walletDao.showBalance(userName);
	}
	
	/*
	 * Method: deposit 
	 * Description: Used to deposit amount into user Account
	 * @param userName: UserName of user account
	 * @param password: Password of user account
	 * @param amount: Amount being deposited by the user
	 * @return double: It return a double on success
	 */
	
	@Override
	public double deposit(String userName,String password,double amount) throws ResourceNotFoundException, InvalidLoginCredentialsException {
		return walletDao.deposit(userName,password,amount);
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
	
	@Override
	public double withdraw(String userName,String password,double amount) throws ResourceNotFoundException, InsufficientBalanceException, InvalidLoginCredentialsException {
		return walletDao.withdraw(userName,password,amount);
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
	
	@Override
	public double fundTransfer(String userName,String password,int recepientAccountNumber, double amount) throws ResourceNotFoundException, AccountNumberDoesNotExistsException, InsufficientBalanceException, InvalidLoginCredentialsException {
		return walletDao.fundTransfer(userName,password,recepientAccountNumber,amount);
	}

	/*
	 * Method: getAllTransactions 
	 * Description: Used to get all the transactions done by the user
	 * @param userName: UserName of user account
	 * @return list: It returns a list of transactions on success
	 */
	
	@Override
	public List<Passbook> getAllTransactions(String userName) throws ResourceNotFoundException {
		return walletDao.getAllTransactions(userName);
	}

	/*
	 * Method: loginValidate 
	 * Description: Used to transfer amount from one Account to other Account
	 * @param userName: UserName of user account
	 * @param password: Password of user account
	 * @return boolean: It return true on success
	 * @throws InvalidLoginCredentialsException : It is raised if Login Credentials are not Valid
	 */
	
	@Override
	public boolean loginValidate(String userName,String password) throws ResourceNotFoundException, InvalidLoginCredentialsException {
		return walletDao.loginValidate(userName,password);
	}
	
	/*
	 * Method: editAccount 
	 * Description: Used to edit details of the account
	 * @param account: Edited account details
	 */
	
	@Override
	public void editAccount(Account account) {
		walletDao.editAccount(account);
		
	}

	/*
	 * Method: deleteAccount 
	 * Description: Used to delete Account of the user from database 
	 * @param userName: UserName of user account
	 */
	
	@Override
	public void deleteAccount(String userName) throws ResourceNotFoundException {
		walletDao.deleteAccount(userName);
	}
	
}
