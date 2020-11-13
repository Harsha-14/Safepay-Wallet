package com.cg.safepaywallet.bean;

import java.util.List;

import javax.persistence.CascadeType;

//import java.util.ArrayList;
//import java.util.List;
//import java.util.List;

//import javax.persistence.CascadeType;
//import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="ACCOUNT")
public class Account{
	@NotEmpty(message="Name should not be empty")
	@Column(name="NAME")
	@Length(max=30)
	public String name;

	@NotEmpty(message="City should not be empty")
	@Column(name="CITY")
	@Length(max=20)
	public String city;
	
	@NotEmpty(message="Mobile number should not be empty")
	@Column(name="PHONENUMBER")
	@Length(max=15)
	public String phoneNumber;
	
	@NotEmpty(message="Password should not be empty")
	@Column(name="PASSWORD")
	@Length(max=15)
	public String password;
	
	@NotEmpty(message="User Name should not be empty")
	@Column(name="USERNAME")
	@Length(max=15)
	public String userName;
	
	@NotEmpty(message="Email should not be empty")
	@Column(name="EMAIL")
	@Length(max=50)
	public String email;
	
	@Id
	@Column(name="ACCOUNTNUMBER")
	public int accountNumber;
	
	@Column(name="AGE")
	public int age;
	
	@Column(name="BALANCE")
	public double balance;
	
	@JsonBackReference
	@OneToMany(cascade=CascadeType.ALL)
	public List<Passbook> transactions;
	
	
	
	public Account() {
		
	}
	public Account(String name, String city,int age, String phoneNumber,String email,String userName,String password) {
		super();
		this.name = name;
		this.city = city;
		this.age=age;
		this.email=email;
		this.phoneNumber = phoneNumber;
		this.password=password;
		this.userName=userName;

	}

public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public List<Passbook> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<Passbook> transactions) {
		this.transactions = transactions;
	}

@Override
	public String toString() {
		return "Account [name=" + name + ", city=" + city + ", phoneNumber=" + phoneNumber + ", password=" + password
				+ ", userName=" + userName + ", email=" + email + ", accountNumber=" + accountNumber + ", age=" + age
				+ ", balance=" + balance + "]";
	}
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void addTransaction(Passbook transaction) {
		transaction.setAccount(this);
		this.getTransactions().add(transaction);
	
	}
	
}
