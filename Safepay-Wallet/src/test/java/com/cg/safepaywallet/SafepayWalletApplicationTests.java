package com.cg.safepaywallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cg.safepaywallet.bean.Account;
import com.cg.safepaywallet.exceptions.AccountNumberDoesNotExistsException;
import com.cg.safepaywallet.exceptions.InsufficientBalanceException;
import com.cg.safepaywallet.exceptions.InvalidLoginCredentialsException;
import com.cg.safepaywallet.exceptions.MobileNumberRegisteredException;
import com.cg.safepaywallet.exceptions.ResourceNotFoundException;
import com.cg.safepaywallet.exceptions.UserNameExistsException;
import com.cg.safepaywallet.service.WalletServiceImpl;

@SpringBootTest
class SafepayWalletApplicationTests {

	@Autowired
	WalletServiceImpl service;


	@Test
	public void test11() throws MobileNumberRegisteredException, UserNameExistsException {
		int accountNum=service.addAccount(new Account("Jyothi", "Kakinada", 37,"9550259189", "jyothi@gmail","Jyothi@1","Jyothi@1"));
		assertEquals(1113,accountNum);
	
	}
	
	@Test
	public void test12() throws MobileNumberRegisteredException, UserNameExistsException {
		assertThrows(MobileNumberRegisteredException.class,()->
		{
			service.addAccount(new Account("Jyothi", "Kakinada", 37,"9550259189", "jyothi@gmail","Jyothi@15","Jyothi@15"));
		});
	}
	
	@Test
	public void test13() throws MobileNumberRegisteredException, UserNameExistsException {
		assertThrows(UserNameExistsException.class,()->
		{
			service.addAccount(new Account("Jyothi", "Kakinada", 37,"9550259188", "jyothi@gmail","Jyothi@1","Jyothi@1"));
		});
	}

	@Test
	public void test14() throws ResourceNotFoundException, InvalidLoginCredentialsException {
		double amt=service.deposit("Jyothi@1","Jyothi@1",200);
		assertEquals(200,amt);
	}

	@Test
	public void test15() throws ResourceNotFoundException, InvalidLoginCredentialsException {
		assertThrows(InvalidLoginCredentialsException.class,()->
		{
			service.deposit("Jyothi@1","Jyothi",200);
		});
	}

	@Test
	public void test16() throws ResourceNotFoundException, InvalidLoginCredentialsException, InsufficientBalanceException {
		double amt=service.withdraw("Jyothi@1","Jyothi@1",100);
		assertEquals(100,amt);
	}

	@Test
	public void test17() throws ResourceNotFoundException, InvalidLoginCredentialsException, InsufficientBalanceException {
		assertThrows(InvalidLoginCredentialsException.class,()->
		{
			service.withdraw("Jyothi@1","Jyothi",100);
		});
	}

	@Test
	public void test18() throws ResourceNotFoundException, InvalidLoginCredentialsException, InsufficientBalanceException {
		assertThrows(InsufficientBalanceException.class,()->
		{
			service.withdraw("Jyothi@1","Jyothi@1",50000);
		});
	}


	@Test
	public void test19() throws ResourceNotFoundException, InvalidLoginCredentialsException {
		assertThrows(InvalidLoginCredentialsException.class,()->
		{
			service.fundTransfer("Jyothi@1","Jyothi",1111,100);
		});
	}
	
	@Test
	public void test20() throws ResourceNotFoundException, InvalidLoginCredentialsException, AccountNumberDoesNotExistsException, InsufficientBalanceException {
		assertThrows(InsufficientBalanceException.class,()->
		{
			service.fundTransfer("Jyothi@1","Jyothi@1",1111,50000);
		});
	}
	
	@Test
	public void test21() throws ResourceNotFoundException, InvalidLoginCredentialsException, AccountNumberDoesNotExistsException, InsufficientBalanceException {
		assertThrows(AccountNumberDoesNotExistsException.class,()->
		{
			service.fundTransfer("Jyothi@1","Jyothi@1",1,100);
		});
	}
	
	@Test
	public void test22() throws ResourceNotFoundException, InvalidLoginCredentialsException, AccountNumberDoesNotExistsException, InsufficientBalanceException {
		double amt=service.fundTransfer("Jyothi@1","Jyothi@1",1111,100);
		assertEquals(100,amt);
	}
	
	@Test
	public void test23() throws ResourceNotFoundException, InvalidLoginCredentialsException {
		double amt=service.showBalance("Jyothi@1");
		assertEquals(0,amt);
	}
	@Test
	public void test24() throws ResourceNotFoundException {
		service.deleteAccount("Jyothi@1");
	}
	
}
