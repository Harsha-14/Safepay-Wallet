package com.cg.safepaywallet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.safepaywallet.bean.Account;
import com.cg.safepaywallet.bean.Passbook;
import com.cg.safepaywallet.exceptions.AccountNumberDoesNotExistsException;
import com.cg.safepaywallet.exceptions.InsufficientBalanceException;
import com.cg.safepaywallet.exceptions.InvalidLoginCredentialsException;
import com.cg.safepaywallet.exceptions.MobileNumberRegisteredException;
import com.cg.safepaywallet.exceptions.ResourceNotFoundException;
import com.cg.safepaywallet.exceptions.UserNameExistsException;
import com.cg.safepaywallet.security.AuthenticationRequest;
import com.cg.safepaywallet.security.AuthenticationResponse;
import com.cg.safepaywallet.security.JwtUtil;
import com.cg.safepaywallet.security.MyUserDetailsService;
import com.cg.safepaywallet.service.WalletServiceImpl;

@CrossOrigin(origins = "http://localhost:4200")
//@CrossOrigin
@RestController
@RequestMapping("/abc")

public class WalletController {

	@Autowired
	public WalletServiceImpl service;
	Logger logger=LoggerFactory.getLogger(WalletController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private MyUserDetailsService userDetailsService;

	@RequestMapping({"/hello"})
	
		public String hello(HttpServletRequest request){
		final String token = request.getHeader("Authorization");			
		final String username = jwtTokenUtil.extractUsername(token.substring(7));
		

			return username;
		}
	

	
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}


		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}


//	final String token = request.getHeader("Authorization");			
//	final String username = jwtUtil.extractUsername(token.substring(7));

	
	
//	eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoIiwiZXhwIjoxNTk3MjY0MzE3LCJpYXQiOjE1OTcwNDgzMTd9.wUeoy3LBDZBHCXoD3Fdv6S4o20vSRcpj5HjmAezSMJU
	
//	eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhIiwiZXhwIjoxNTk3MjY0NjkyLCJpYXQiOjE1OTcwNDg2OTJ9.f3PUecJYias6U8KY0HCX0fX6o1qig_mr9zkU7_dKtYI
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/*
 * Type: Get mapping
 * Method: getAllAccounts
 * Description: Used to get all account details
 * @return list: It returns a list
 */		
	
	@GetMapping("/allUsers")
	public List<Account> getAllAccounts() {
		logger.info("Admin Requested For the Details of All the Users");
		return service.getAll();
	}
	
/*
 * Type: Get mapping
 * Method: getSingleAccount 
 * Description: Used to get account based on username
 * @param userName: UserName of user account
 * @return account: It return a user account on success
 * @throws ResourceNotFoundException : It is raised if User doesnot exists with given username
 * 
 */		
	
	@GetMapping("/user/{userName}")
	public Account getSingleAccount(@PathVariable String userName) throws ResourceNotFoundException{
		logger.info("User Requested For the Account Details");
			return service.getAccount(userName);
	}

	/*
	 * Type: Get mapping
	 * Method: showBalance 
	 * Description: Used to get balance amount in user Account
	 * @param userName: UserName of user account
	 * @return double: It return a double on success
	 */

	@GetMapping("/balance/{userName}")
	public double getBalance(@PathVariable String userName) throws ResourceNotFoundException {
		logger.info("User Requested For the Balance in the Account");
			return service.showBalance(userName);
	}

	/*
	 * Method: loginValidate 
	 * Type: Get mapping
	 * Description: Used to transfer amount from one Account to other Account
	 * @param userName: UserName of user account
	 * @param password: Password of user account
	 * @return boolean: It return true on success
	 * @throws InvalidLoginCredentialsException : It is raised if Login Credentials are not Valid
	 */
	
	@GetMapping("/pass/{userName}/{password}")
	public boolean loginValidate(@PathVariable String userName, @PathVariable String password) throws InvalidLoginCredentialsException, ResourceNotFoundException{
		logger.info("User Requested for Login Validation");
		return service.loginValidate(userName, password);
	}
	
	/*
	 * Method: deposit 
	 * Type: Get mapping
	 * Description: Used to deposit amount into user Account
	 * @param userName: UserName of user account
	 * @param password: Password of user account
	 * @param amount: Amount being deposited by the user
	 * @return double: It return a double on success
	 */
	
	@GetMapping("/deposit/{userName}/{password}/{amount}")
	public double deposit(@PathVariable String userName,@PathVariable String password, @PathVariable double amount) throws ResourceNotFoundException, InvalidLoginCredentialsException {
		logger.info("User Requested For the Deposit of Amount");
			return service.deposit(userName,password, amount);
	}
	
	/*
	 * Type: Get mapping
	 * Method: withdraw 
	 * Description: Used to withdraw amount from user Account
	 * @param userName: UserName of user account
	 * @param password: Password of user account
	 * @param amount: Amount being transferred by the user
	 * @return double: It return a double on success
	 * @throws InsufficientBalanceException : It is raised if User doesnot have
	 * Sufficient Amount to transfer
	 */
	
	@GetMapping("/withdraw/{userName}/{password}/{amount}")
	public double withdraw(@PathVariable String userName,@PathVariable String password, @PathVariable double amount) throws ResourceNotFoundException, InsufficientBalanceException, InvalidLoginCredentialsException {
		logger.info("User Requested For the Withdraw of Amount");
			return service.withdraw(userName,password, amount);
	}

	/*
	 * Method: fundTransfer 
	 * Type: Get mapping
	 * Description: Used to transfer amount from one Account to other Account
	 * @param userName: UserName of user account
	 * @param password: Password of user account
	 * @param amount: Amount being transferred by the user
	 * @param recepientAccountNumber: Account Number of the Recepient
	 * @return double: It return a double on success
	 * @throws AccountNumberDoesnotExistsException : It is raised if Recepient Account Number is not Valid
	 * @throws InsufficientBalanceException : It is raised if User doesnot have Sufficient Amount to transfer
	 */
	
	@GetMapping("/ft/{userName}/{password}/{recepientAccountNumber}/{amount}")
	public double fundTransfer(@PathVariable String userName,@PathVariable String password, @PathVariable int recepientAccountNumber, @PathVariable double amount) throws ResourceNotFoundException, AccountNumberDoesNotExistsException, InsufficientBalanceException, InvalidLoginCredentialsException {
		logger.info("User Requested For the Transfer of Amount");
		double d=service.fundTransfer(userName,password,recepientAccountNumber,amount);
		return d;
	}

	/*
	 * Method: getAllTransactions 
	 * Type: Get mapping
	 * Description: Used to get all the transactions done by the user
	 * @param userName: UserName of user account
	 * @return list: It returns a list of transactions on success
	 */
	
	@GetMapping("/passbook/{userName}")
	public List<Passbook> getAllTransactions(@PathVariable String userName) throws ResourceNotFoundException {
		logger.info("User Requested For the Details of All the Transactions Done");
			return service.getAllTransactions(userName);
	}

	/*
	 * Type: Post mapping
	 * Method: addAccount 
	 * Description: Used for user registration /sign up 
	 * @param account: account details
	 * @return int: returns account number generated after registration
	 * 
	 */
	
	@PostMapping("/add")
	public int addAccount(@Valid @RequestBody Account account) throws MobileNumberRegisteredException, UserNameExistsException{
		logger.info("User Requested For the Creation of Account");
		return service.addAccount(account);
	}
	
	/*
	 * Method: editAccount 
	 * Type: Put mapping
	 * Description: Used to edit details of the account
	 * @param account: Edited account details
	 */
	
	@PutMapping("/edit/{userName}")
	public Account editAccount(@Valid @RequestBody Account account, @PathVariable String userName) {
		logger.info("User Requested To Update Details of the Account");
		service.editAccount(account);
		return account;
	}

	/*
	 * Method: deleteAccount 
	 * Type: Delete mapping
	 * Description: Used to delete Account of the user from database 
	 * @param userName: UserName of user account
	 */
	
	@DeleteMapping("/delete/{userName}")
	public void deleteAccount(@PathVariable String userName) throws ResourceNotFoundException {
		logger.info("Admin Requested to Delete the Account");
		service.deleteAccount(userName);
	}

	
}
