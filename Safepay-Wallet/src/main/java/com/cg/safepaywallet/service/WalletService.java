package com.cg.safepaywallet.service;

import java.util.List;

import com.cg.safepaywallet.bean.Account;
import com.cg.safepaywallet.bean.Passbook;
import com.cg.safepaywallet.exceptions.AccountNumberDoesNotExistsException;
import com.cg.safepaywallet.exceptions.InsufficientBalanceException;
import com.cg.safepaywallet.exceptions.InvalidLoginCredentialsException;
import com.cg.safepaywallet.exceptions.MobileNumberRegisteredException;
import com.cg.safepaywallet.exceptions.ResourceNotFoundException;
import com.cg.safepaywallet.exceptions.UserNameExistsException;

public interface WalletService {
	public int addAccount(Account account) throws MobileNumberRegisteredException, UserNameExistsException;
	public double showBalance(String userName) throws ResourceNotFoundException;
	public double deposit(String userName,String password,double amount) throws ResourceNotFoundException, InvalidLoginCredentialsException;
	public double withdraw(String userName,String password,double amount) throws ResourceNotFoundException, InsufficientBalanceException, InvalidLoginCredentialsException;
	public double fundTransfer(String userName,String password,int recepientAccountNumber, double amount) throws ResourceNotFoundException, AccountNumberDoesNotExistsException, InsufficientBalanceException, InvalidLoginCredentialsException;
	public List<Passbook> getAllTransactions(String userName) throws ResourceNotFoundException;

	
	public List<Account> getAll();
	public Account getAccount(String userName) throws ResourceNotFoundException;
	public void editAccount(Account account);
	public void deleteAccount(String userName) throws ResourceNotFoundException;

	
	public boolean loginValidate(String userName,String password) throws ResourceNotFoundException, InvalidLoginCredentialsException;

}
