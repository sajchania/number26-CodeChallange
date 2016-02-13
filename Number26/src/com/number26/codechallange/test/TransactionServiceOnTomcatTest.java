package com.number26.codechallange.test;

import org.junit.Assert;
import org.junit.Test;

import com.number26.codechallange.main.Transaction;

/**
 * Before running this tests, the Tomcat server should be started at localhost<br>
 * 
 * <b>Tested & validated on: </b><br>
 * 
 * Server version: Apache Tomcat/7.0.65 <br>
 * Server built: Oct 9 2015 08:36:58 UTC OS <br>
 * Name: Windows 7 <br>
 * OS Version: 6.1 <br>
 * Architecture: amd64 <br>
 * JVM Version: 1.8.0_05-b13 <br>
 * 
 * @author Jacek Galewicz
 */
public class TransactionServiceOnTomcatTest extends
		TransactionServiceOnTomcatTestBase {

	@Test
	public void simpleExampleFromCodeChallangeTest() {
		// first example
		Transaction transaction1 = new Transaction();
		transaction1.setTransaction_id(10);
		transaction1.setAmount(5000);
		transaction1.setType("cars");

		POST_TRANSACTION(transaction1);
		String putExpectation = "PUT transactionservice/transaction/10\n=>{ \"status\": \"ok\" }";
		String putMessage = PUT_TRANSACTION(transaction1);
		Assert.assertEquals(putExpectation, putMessage);

		// second example
		Transaction transaction2 = new Transaction();
		transaction2.setTransaction_id(11);
		transaction2.setAmount(10000);
		transaction2.setParent_id(10);
		transaction2.setType("shopping");

		POST_TRANSACTION(transaction2);
		String putExpectation2 = "PUT transactionservice/transaction/11\n=>{ \"status\": \"ok\" }";
		String putMessage2 = PUT_TRANSACTION(transaction2);
		Assert.assertEquals(putExpectation2, putMessage2);

		// third exammple
		String getExpectation = "GET transactionservice/types/cars\n=> [ 10 ]";
		String getMessage = GET_TYPE(transaction1);
		Assert.assertEquals(getExpectation, getMessage);

		// fourth exammple
		String getExpectation2 = "GET transactionservice/sum/10\n=> { \"sum \": 15000.0 }";
		String getMessage2 = GET_SUM(transaction1);
		Assert.assertEquals(getExpectation2, getMessage2);

		// fifth exammple
		String getExpectation3 = "GET transactionservice/sum/11\n=> { \"sum \": 10000.0 }";
		String getMessage3 = GET_SUM(transaction2);
		Assert.assertEquals(getExpectation3, getMessage3);
	}

	@Test
	public void simplePostGetTest() {

		String transaction_id = "98";
		Transaction transaction1 = new Transaction();
		transaction1.setTransaction_id(Long.parseLong(transaction_id));
		transaction1.setAmount(1.0);
		transaction1.setParent_id(1);
		transaction1.setType("TYP1");

		String postExpectation = "POST transactionservice/transaction/98\n=>{ \"status\": \"ok\" }";
		String postMessage = POST_TRANSACTION(transaction1);
		Assert.assertEquals(postExpectation, postMessage);

		String getExpectation = "GET transactionservice/transaction/98\n{ \"amount\": 1.0, \"type\": TYP1, \"parent_id\": 1 }\n=>{ \"status\": \"ok\" }";
		String getMessage = GET_TRANSACTION(transaction1);
		Assert.assertEquals(getExpectation, getMessage);
	}

	@Test
	public void simplePostPutGetTest() {

		String transaction_id = "98";
		Transaction transaction1 = new Transaction();
		transaction1.setTransaction_id(Long.parseLong(transaction_id));
		transaction1.setAmount(1.0);
		transaction1.setParent_id(1);
		transaction1.setType("TYP1");

		String postExpectation = "POST transactionservice/transaction/98\n=>{ \"status\": \"ok\" }";
		String postMessage = POST_TRANSACTION(transaction1);
		Assert.assertEquals(postExpectation, postMessage);

		transaction1.setAmount(2.0);
		transaction1.setParent_id(10);
		transaction1.setType("TYP20");

		String putExpectation = "PUT transactionservice/transaction/98\n=>{ \"status\": \"ok\" }";
		String putMessage = PUT_TRANSACTION(transaction1);
		Assert.assertEquals(putExpectation, putMessage);

		String getExpectation = "GET transactionservice/transaction/98\n{ \"amount\": 2.0, \"type\": TYP20, \"parent_id\": 10 }\n=>{ \"status\": \"ok\" }";
		String getMessage = GET_TRANSACTION(transaction1);
		Assert.assertEquals(getExpectation, getMessage);
	}

	@Test
	public void simplePostGetWithoutParentIdTest() {

		String transaction_id = "98";
		Transaction transaction1 = new Transaction();
		transaction1.setTransaction_id(Long.parseLong(transaction_id));
		transaction1.setAmount(1.0);
		transaction1.setType("TYP1");

		String postExpectation = "POST transactionservice/transaction/98\n=>{ \"status\": \"ok\" }";
		String postMessage = POST_TRANSACTION(transaction1);
		Assert.assertEquals(postExpectation, postMessage);

		String getExpectation = "GET transactionservice/transaction/98\n{ \"amount\": 1.0, \"type\": TYP1 }\n=>{ \"status\": \"ok\" }";
		String getMessage = GET_TRANSACTION(transaction1);
		Assert.assertEquals(getExpectation, getMessage);
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
		String postExpectation2 = "POST transactionservice/transaction/0\n{ }";
		String postMessage2 = POST_TRANSACTION(transaction1);
		Assert.assertEquals(postExpectation2, postMessage2);

	}

	@Test
	public void wrongPostTest() {

		String transaction_id = "98";
		Transaction transaction1 = new Transaction();
		transaction1.setTransaction_id(Long.parseLong(transaction_id));
		transaction1.setAmount(1.0);
		transaction1.setParent_id(1);
		transaction1.setType("TYP1");

		String postExpectation = "POST transactionservice/transaction/98\n=>{ \"status\": \"ok\" }";
		String postMessage = POST_TRANSACTION(transaction1);
		Assert.assertEquals(postExpectation, postMessage);

		// In tomcat console we can see the message:
		// "SEVERE: Transaction with an id: 98 exists" as expected
		String postExpectation2 = "POST transactionservice/transaction/98\n{ }";
		String postMessage2 = POST_TRANSACTION(transaction1);
		Assert.assertEquals(postExpectation2, postMessage2);

	}

	@Test
	public void wrongPutTest() {

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
		String putExpectation = "PUT transactionservice/transaction/98\n{ }";
		String putMessage = PUT_TRANSACTION(transaction1);
		Assert.assertEquals(putExpectation, putMessage);
	}

	@Test
	public void noGetTest() {

		String transaction_id = "98";
		Transaction transaction1 = new Transaction();
		transaction1.setTransaction_id(Long.parseLong(transaction_id));
		transaction1.setAmount(1.0);
		transaction1.setParent_id(1);
		transaction1.setType("TYP1");

		String postExpectation = "POST transactionservice/transaction/98\n=>{ \"status\": \"ok\" }";
		String postMessage = POST_TRANSACTION(transaction1);
		Assert.assertEquals(postExpectation, postMessage);

		String transaction_id2 = "99";
		Transaction unknown_transaction = new Transaction();
		unknown_transaction.setTransaction_id(Long.parseLong(transaction_id2));
		unknown_transaction.setAmount(1.0);
		unknown_transaction.setParent_id(1);
		unknown_transaction.setType("TYP1");

		// In tomcat console we can see the message:
		// "SEVERE: Transaction with an id: 99 was not found" as expected
		String getExpectation = "GET transactionservice/transaction/99\n{ }";
		String getMessage = GET_TRANSACTION(unknown_transaction);
		Assert.assertEquals(getExpectation, getMessage);
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

		String postExpectation = "POST transactionservice/transaction/98\n=>{ \"status\": \"ok\" }";
		String postMessage = POST_TRANSACTION(transaction1);
		Assert.assertEquals(postExpectation, postMessage);

		String postExpectation2 = "POST transactionservice/transaction/99\n=>{ \"status\": \"ok\" }";
		String postMessage2 = POST_TRANSACTION(transaction2);
		Assert.assertEquals(postExpectation2, postMessage2);

		String postExpectation3 = "POST transactionservice/transaction/100\n=>{ \"status\": \"ok\" }";
		String postMessage3 = POST_TRANSACTION(transaction3);
		Assert.assertEquals(postExpectation3, postMessage3);

		String typeExpectation1 = "GET transactionservice/types/TYP1\n=> [ 98, 99, 100 ]";
		String typeMessage1 = GET_TYPE(transaction1);
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

		String postExpectation = "POST transactionservice/transaction/98\n=>{ \"status\": \"ok\" }";
		String postMessage = POST_TRANSACTION(transaction1);
		Assert.assertEquals(postExpectation, postMessage);

		String postExpectation2 = "POST transactionservice/transaction/99\n=>{ \"status\": \"ok\" }";
		String postMessage2 = POST_TRANSACTION(transaction2);
		Assert.assertEquals(postExpectation2, postMessage2);

		String postExpectation3 = "POST transactionservice/transaction/100\n=>{ \"status\": \"ok\" }";
		String postMessage3 = POST_TRANSACTION(transaction3);
		Assert.assertEquals(postExpectation3, postMessage3);

		String transaction_id4 = "100";
		Transaction unknown_transaction = new Transaction();
		unknown_transaction.setTransaction_id(Long.parseLong(transaction_id4));
		unknown_transaction.setAmount(2.0);
		unknown_transaction.setParent_id(3);
		unknown_transaction.setType("TYP4");

		String typeExpectation1 = "GET transactionservice/types/TYP4\n=> [  ]";
		String typeMessage1 = GET_TYPE(unknown_transaction);
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

		String postExpectation = "POST transactionservice/transaction/98\n=>{ \"status\": \"ok\" }";
		String postMessage = POST_TRANSACTION(transaction1);
		Assert.assertEquals(postExpectation, postMessage);

		String postExpectation2 = "POST transactionservice/transaction/99\n=>{ \"status\": \"ok\" }";
		String postMessage2 = POST_TRANSACTION(transaction2);
		Assert.assertEquals(postExpectation2, postMessage2);

		String postExpectation3 = "POST transactionservice/transaction/100\n=>{ \"status\": \"ok\" }";
		String postMessage3 = POST_TRANSACTION(transaction3);
		Assert.assertEquals(postExpectation3, postMessage3);

		// In tomcat console we can see the message:
		// "SEVERE: There are no transactions with the type: TYP4." as expected
		String sumExpectation1 = "GET transactionservice/sum/98\n=> { \"sum \": 8.0 }";
		String sumMessage1 = GET_SUM(transaction1);
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

		String postExpectation = "POST transactionservice/transaction/98\n=>{ \"status\": \"ok\" }";
		String postMessage = POST_TRANSACTION(transaction1);
		Assert.assertEquals(postExpectation, postMessage);

		String sumExpectation1 = "GET transactionservice/sum/98\n=> { \"sum \": 1.0 }";
		String sumMessage1 = GET_SUM(transaction1);
		Assert.assertEquals(sumExpectation1, sumMessage1);
	}

	@Test
	public void noParentSumTest() {

		String transaction_id = "98";
		Transaction unknown_transaction = new Transaction();
		unknown_transaction.setTransaction_id(Long.parseLong(transaction_id));
		unknown_transaction.setAmount(2.0);
		unknown_transaction.setParent_id(Long.parseLong(transaction_id));
		unknown_transaction.setType("TYP2");

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

		String postExpectation2 = "POST transactionservice/transaction/99\n=>{ \"status\": \"ok\" }";
		String postMessage2 = POST_TRANSACTION(transaction2);
		Assert.assertEquals(postExpectation2, postMessage2);

		String postExpectation3 = "POST transactionservice/transaction/100\n=>{ \"status\": \"ok\" }";
		String postMessage3 = POST_TRANSACTION(transaction3);
		Assert.assertEquals(postExpectation3, postMessage3);

		// In tomcat console we can see the message:
		// "SEVERE: There is no transaction with transactionId 98." as expected
		String sumExpectation1 = "GET transactionservice/sum/98\n=> { \"sum \":  }";
		String sumMessage1 = GET_SUM(unknown_transaction);
		Assert.assertEquals(sumExpectation1, sumMessage1);
	}

}
