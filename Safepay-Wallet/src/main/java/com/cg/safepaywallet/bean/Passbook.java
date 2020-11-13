package com.cg.safepaywallet.bean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;


@Entity
@Table(name="PASSBOOK")
public class Passbook implements Comparable<Passbook>{

	@Id
	@Column(name="TRANSACTIONID")
	@Length(max=10)
	public String transactionId;
	@Column(name="ACTION")
	@Length(max=20)
	public String action;
	@Column(name="AMOUNT")
	public double amount;
	@Column(name="BALANCE")
	public double balance;
	@Column(name="DAY")
	@Length(max=20)
	public String day;
	@Column(name="TIME")
	@Length(max=20)
	public String time;

	@ManyToOne
	public Account account;

	public Passbook() {
		
	}

	public String getTransactionId() {
		return transactionId;
	}
	public Passbook(String transactionId, String action, double amount, double balance) {
	super();
	  DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  
	   LocalDateTime now = LocalDateTime.now();  

	this.transactionId = transactionId;
	this.action = action;
	this.amount = amount;
	this.balance = balance;
	this.day=dtf.format(now).substring(0, 10);
	this.time=dtf.format(now).substring(11);
	
}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getDay() {
		return day;
	}


	public void setDay(String day) {
		this.day = day;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "Passbook [transactionId=" + transactionId + ", action=" + action + ", amount=" + amount + ", balance=" + balance + ", day="
				+ day + ", time=" + time + ", account=" + account + "]";
	}

	@Override
	public int compareTo(Passbook o) {
			return this.transactionId.compareTo(o.transactionId);
		
	}


}
