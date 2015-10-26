package com.number26.codechallange.main;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Transaction class
 * 
 * @author Jacek Galewicz
 * 
 */
@XmlRootElement(name = "transaction")
public class Transaction {
	private long transaction_id;
	private double amount;
	private String type;
	private long parent_id = 0;

	public Transaction() {
	}

	public Transaction(long transaction_id, double amount, String type,
			long parent_id) {
		this.transaction_id = transaction_id;
		this.amount = amount;
		this.type = type;
		this.parent_id = parent_id;
	}

	public Transaction(long transaction_id, double amount, String type) {
		this.transaction_id = transaction_id;
		this.amount = amount;
		this.type = type;
	}

	public long getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(long transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getParent_id() {
		return parent_id;
	}

	public void setParent_id(long parent_id) {
		this.parent_id = parent_id;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}