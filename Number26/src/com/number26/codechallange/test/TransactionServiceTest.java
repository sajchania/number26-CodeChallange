package com.number26.codechallange.test;

import java.util.HashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.number26.codechallange.main.Transaction;
import com.number26.codechallange.main.TransactionService;

/**
 * Unit Tests for class {@link TransactionService}
 * 
 * @author Jacek Galewicz
 * 
 */
public class TransactionServiceTest {
	private TransactionService t = new TransactionService();
	HashMap<Long, Transaction> transactions = new HashMap<Long, Transaction>();
	Transaction result = new Transaction();

	@Before
	public void setup() {
		t = new TransactionService();
		transactions = new HashMap<Long, Transaction>();
		result = new Transaction();
	}

	@After
	public void cleanup() {
		DELETE(t);
	}

	@Test
	public void createTransactionTest() {

		String transaction_id = "98";
		Long id = Long.parseLong(transaction_id);
		Transaction transaction1 = new Transaction();
		transaction1.setTransaction_id(Long.parseLong(transaction_id));
		transaction1.setAmount(1.0);
		transaction1.setParent_id(1);
		transaction1.setType("TYP1");

		POST(transaction1);
		GET(id);
		ASSERT_EQUALS(transaction1, result);
	}

	@Test
	public void createAndRemoveTransactionTest() {

		String transaction_id = "98";
		Transaction transaction1 = new Transaction();
		transaction1.setTransaction_id(Long.parseLong(transaction_id));
		transaction1.setAmount(1.0);
		transaction1.setParent_id(1);
		transaction1.setType("TYP1");

		POST(transaction1);
		Assert.assertEquals(1, transactions.size());
		DELETE(t);
		Assert.assertEquals(0, transactions.size());
	}

	@Test
	public void simplePostPutGetTest() {

		String transaction_id = "98";
		Transaction transaction1 = new Transaction();
		transaction1.setTransaction_id(Long.parseLong(transaction_id));
		transaction1.setAmount(1.0);
		transaction1.setParent_id(1);
		transaction1.setType("TYP1");

		POST(transaction1);
		Assert.assertEquals(1, transactions.size());

		transaction1.setAmount(2.0);
		transaction1.setParent_id(10);
		transaction1.setType("TYP20");

		PUT(transaction1, t, transactions);
		GET(transaction_id);
		ASSERT_EQUALS(transaction1, result);

	}

	@Test
	public void simplePostGetWithoutParentIdTest() {

		String transaction_id = "98";
		Transaction transaction1 = new Transaction();
		transaction1.setTransaction_id(Long.parseLong(transaction_id));
		transaction1.setAmount(1.0);
		transaction1.setType("TYP1");

		POST(transaction1);
		GET(transaction_id);
		ASSERT_EQUALS(transaction1, result);
	}

	@Test
	public void wrongIdTest() {

		String transaction_id = "0";
		Transaction transaction1 = new Transaction();
		transaction1.setTransaction_id(Long.parseLong(transaction_id));
		transaction1.setAmount(1.0);
		transaction1.setParent_id(1);
		transaction1.setType("TYP1");

		// In tomcat console we can see the message:
		// "SEVERE: Transaction can't have an id that is equal to 0" as expected

		POST(transaction1);
		GET(transaction_id);
		Assert.assertEquals(0, transactions.size());

	}

	@Test
	public void wrongPostTest() {

		Transaction badResult = new Transaction(0, 0, null, 0);

		String transaction_id = "98";
		Transaction transaction1 = new Transaction();
		transaction1.setTransaction_id(Long.parseLong(transaction_id));
		transaction1.setAmount(1.0);
		transaction1.setParent_id(1);
		transaction1.setType("TYP1");

		// In tomcat console we can see the message:
		// "SEVERE: Transaction with an id: 98 exists" as expected
		POST(transaction1);
		POST(transaction1);

		ASSERT_EQUALS(badResult, result);
	}

	@Test
	public void wrongPutTest() {

		Transaction badResult = new Transaction(0, 0, null, 0);

		String transaction_id = "98";
		Transaction transaction1 = new Transaction();
		transaction1.setTransaction_id(Long.parseLong(transaction_id));
		transaction1.setAmount(1.0);
		transaction1.setParent_id(1);
		transaction1.setType("TYP1");

		transaction1.setAmount(2.0);
		transaction1.setParent_id(10);
		transaction1.setType("TYP20");

		// In tomcat console we can see the message:
		// "SEVERE: Transaction with an id: 98 does not exists" as expected

		PUT(transaction1, t, transactions);
		ASSERT_EQUALS(badResult, result);
	}

	@Test
	public void noGetTest() {

		Transaction badResult = new Transaction(0, 0, null, 0);

		String transaction_id = "98";
		Transaction transaction1 = new Transaction();
		transaction1.setTransaction_id(Long.parseLong(transaction_id));
		transaction1.setAmount(1.0);
		transaction1.setParent_id(1);
		transaction1.setType("TYP1");

		// In tomcat console we can see the message:
		// "SEVERE: Transaction with an id: 99 was not found" as expected
		try {
			POST(transaction1);

			GET("99");
			ASSERT_EQUALS(badResult, result);
		} catch (Exception e) {
			Assert.assertEquals(null, e.getMessage());
		}

	}

	@Test
	public void simpleTypeTest() {

		String transaction_id = "98";
		Transaction transaction1 = new Transaction();
		transaction1.setTransaction_id(Long.parseLong(transaction_id));
		transaction1.setAmount(1.0);
		transaction1.setParent_id(1);
		transaction1.setType("TYP1");

		String transaction_id2 = "99";
		Transaction transaction2 = new Transaction();
		transaction2.setTransaction_id(Long.parseLong(transaction_id2));
		transaction2.setAmount(2.0);
		transaction2.setParent_id(3);
		transaction2.setType("TYP1");

		String transaction_id3 = "100";
		Transaction transaction3 = new Transaction();
		transaction3.setTransaction_id(Long.parseLong(transaction_id3));
		transaction3.setAmount(2.0);
		transaction3.setParent_id(3);
		transaction3.setType("TYP1");

		POST(transaction1);
		POST(transaction2);
		POST(transaction3);

		String typeExpectation1 = "=> [ 98, 99, 100 ]";
		String typeMessage1 = GET_TYPE("TYP1", t, transactions);
		Assert.assertEquals(typeExpectation1, typeMessage1);

	}

	@Test
	public void NoTypeTest() {

		String transaction_id = "98";
		Transaction transaction1 = new Transaction();
		transaction1.setTransaction_id(Long.parseLong(transaction_id));
		transaction1.setAmount(1.0);
		transaction1.setParent_id(1);
		transaction1.setType("TYP1");

		String transaction_id2 = "99";
		Transaction transaction2 = new Transaction();
		transaction2.setTransaction_id(Long.parseLong(transaction_id2));
		transaction2.setAmount(2.0);
		transaction2.setParent_id(3);
		transaction2.setType("TYP1");

		String transaction_id3 = "100";
		Transaction transaction3 = new Transaction();
		transaction3.setTransaction_id(Long.parseLong(transaction_id3));
		transaction3.setAmount(2.0);
		transaction3.setParent_id(3);
		transaction3.setType("TYP1");

		POST(transaction1);
		POST(transaction2);
		POST(transaction3);

		String typeExpectation1 = "=> [  ]";
		String typeMessage1 = GET_TYPE("TYP4", t, transactions);
		Assert.assertEquals(typeExpectation1, typeMessage1);

	}

	@Test
	public void simpleSumTest() {

		String transaction_id = "98";
		Transaction transaction1 = new Transaction();
		transaction1.setTransaction_id(Long.parseLong(transaction_id));
		transaction1.setAmount(1.0);
		transaction1.setParent_id(2);
		transaction1.setType("TYP1");

		String transaction_id2 = "99";
		Transaction transaction2 = new Transaction();
		transaction2.setTransaction_id(Long.parseLong(transaction_id2));
		transaction2.setAmount(2.0);
		transaction2.setParent_id(Long.parseLong(transaction_id));
		transaction2.setType("TYP2");

		String transaction_id3 = "100";
		Transaction transaction3 = new Transaction();
		transaction3.setTransaction_id(Long.parseLong(transaction_id3));
		transaction3.setAmount(5.0);
		transaction3.setParent_id(Long.parseLong(transaction_id));
		transaction3.setType("TYP3");

		POST(transaction1);
		POST(transaction2);
		POST(transaction3);

		// In tomcat console we can see the message:
		// "SEVERE: There are no transactions with the type: TYP4." as expected
		String sumExpectation1 = "=> { \"sum \": 8.0 }";
		String sumMessage1 = GET_SUM(transaction_id, t, transactions);
		Assert.assertEquals(sumExpectation1, sumMessage1);
	}

	@Test
	public void singleSumTest() {

		String transaction_id = "98";
		Transaction transaction1 = new Transaction();
		transaction1.setTransaction_id(Long.parseLong(transaction_id));
		transaction1.setAmount(1.0);
		transaction1.setParent_id(2);
		transaction1.setType("TYP1");

		POST(transaction1);

		String sumExpectation1 = "=> { \"sum \": 1.0 }";
		String sumMessage1 = GET_SUM(transaction_id, t, transactions);
		Assert.assertEquals(sumExpectation1, sumMessage1);
	}

	@Test
	public void noParentSumTest() {

		String transaction_id = "98";

		String transaction_id2 = "99";
		Transaction transaction2 = new Transaction();
		transaction2.setTransaction_id(Long.parseLong(transaction_id2));
		transaction2.setAmount(2.0);
		transaction2.setParent_id(Long.parseLong(transaction_id));
		transaction2.setType("TYP2");

		String transaction_id3 = "100";
		Transaction transaction3 = new Transaction();
		transaction3.setTransaction_id(Long.parseLong(transaction_id3));
		transaction3.setAmount(5.0);
		transaction3.setParent_id(Long.parseLong(transaction_id));
		transaction3.setType("TYP3");

		POST(transaction2);
		POST(transaction3);

		// In tomcat console we can see the message:
		// "SEVERE: There is no transaction with transactionId 98." as expected
		String sumExpectation1 = "=> { \"sum \":  }";
		String sumMessage1 = GET_SUM(transaction_id, t, transactions);
		Assert.assertEquals(sumExpectation1, sumMessage1);
	}

	private void POST(Transaction transaction1) {
		t.createTransaction(transaction1);
		transactions = t.getTransactions();
	}

	private void PUT(Transaction transaction1, TransactionService t,
			HashMap<Long, Transaction> transactions) {
		t.updateTransaction(transaction1);
		transactions = t.getTransactions();
	}

	private String GET_TYPE(String s, TransactionService t,
			HashMap<Long, Transaction> transactions) {
		String st = t.getType(s);
		transactions = t.getTransactions();
		return st;
	}

	private String GET_SUM(String transaction_id, TransactionService t,
			HashMap<Long, Transaction> transactions) {
		String st = t.getSum(transaction_id);
		transactions = t.getTransactions();
		return st;
	}

	private void GET(String transaction_id) {
		Long id = Long.parseLong(transaction_id);
		t.getTransaction(transaction_id);
		result = transactions.get(id);
	}

	private void GET(Long transaction_id) {
		t.getTransaction(transaction_id.toString());
		result = transactions.get(transaction_id);
	}

	private void DELETE(TransactionService t) {
		t.deleteTransactions();
		transactions = t.getTransactions();
	}

	private void ASSERT_EQUALS(Transaction expected, Transaction result) {
		Assert.assertEquals(new Long(expected.getTransaction_id()), new Long(
				result.getTransaction_id()));
		Assert.assertEquals(new Double(expected.getAmount()),
				new Double(result.getAmount()));
		Assert.assertEquals(expected.getType(), result.getType());
		Assert.assertEquals(new Long(expected.getParent_id()),
				new Long(result.getParent_id()));
	}

}
