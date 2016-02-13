package com.number26.codechallange.test;

import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import com.number26.codechallange.main.Transaction;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class TransactionServiceOnTomcatTestBase {

	public static final Logger logger = Logger
			.getLogger(TransactionServiceOnTomcatTestBase.class
					.getCanonicalName());
	public static final String SIMPLE_URI = "http://localhost:8080/Number26/codechallange/transactionservice/transaction";
	public static final String TYPE_URI = "http://localhost:8080/Number26/codechallange/transactionservice/types";
	public static final String SUM_URI = "http://localhost:8080/Number26/codechallange/transactionservice/sum";
	public static final String DELETE_URI = "http://localhost:8080/Number26/codechallange/transactionservice/delete";

	protected static Client client;

	@Before
	public void setup() {
		client = Client.create();
	}

	@After
	public void cleanup() {
		DELETE_TRANSACTIONS();
	}

	@Ignore
	protected static String POST_TRANSACTION(Transaction transaction) {
		String message = "";

		WebResource webResource = client.resource(SIMPLE_URI);
		ClientResponse response = webResource.type("application/xml").post(
				ClientResponse.class, transaction);

		String result = response.getEntity(String.class);

		if (response.getStatus() == 200 || response.getStatus() == 201) {
			message = "POST" + " transactionservice/transaction/"
					+ transaction.getTransaction_id() + "\n" + result;
		} else {
			message = "POST failed status: " + response.getStatus();
		}
		return message;

	}

	@Ignore
	protected static String PUT_TRANSACTION(Transaction transaction) {
		String message = "";

		WebResource webResource = client.resource(SIMPLE_URI);
		ClientResponse response = webResource.type("application/xml").put(
				ClientResponse.class, transaction);

		String result = response.getEntity(String.class);

		if (response.getStatus() == 200 || response.getStatus() == 201) {
			message = "PUT" + " transactionservice/transaction/"
					+ transaction.getTransaction_id() + "\n" + result;
		} else {
			message = "PUT failed status: " + response.getStatus();
		}
		return message;
	}

	@Ignore
	protected static String GET_TRANSACTION(Transaction transaction) {
		String message = "";
		String transaction_id = ((Long) transaction.getTransaction_id())
				.toString();

		WebResource webResource = client.resource(SIMPLE_URI);
		ClientResponse response = webResource.path(transaction_id)
				.accept("application/xml").get(ClientResponse.class);

		String result = response.getEntity(String.class);

		if (response.getStatus() == 200) {
			message = "GET" + " transactionservice/transaction/"
					+ transaction_id + "\n" + result;
		} else {
			message = "GET failed status: " + response.getStatus();
		}
		return message;
	}

	@Ignore
	protected static String DELETE_TRANSACTIONS() {
		String message = "";

		WebResource webResource = client.resource(DELETE_URI);
		ClientResponse response = webResource.type("application/xml").delete(
				ClientResponse.class);

		String result = response.getEntity(String.class);

		if (response.getStatus() == 200) {
			message = "GET" + " transactionservice/transaction/" + "\n"
					+ result;
		} else {
			message = "GET failed status: " + response.getStatus();
		}
		return message;
	}

	@Ignore
	protected static String GET_TYPE(Transaction transaction) {
		String message = "";
		String type = transaction.getType();

		WebResource webResource = client.resource(TYPE_URI);
		ClientResponse response = webResource.path(type)
				.accept("application/xml").get(ClientResponse.class);

		String result = response.getEntity(String.class);

		if (response.getStatus() == 200) {
			message = "GET" + " transactionservice/types/" + type + "\n"
					+ result;
		} else {
			message = "GET failed status: " + response.getStatus();
		}
		return message;
	}

	@Ignore
	protected static String GET_SUM(Transaction transaction) {
		String message = "";
		String transaction_id = ((Long) transaction.getTransaction_id())
				.toString();

		WebResource webResource = client.resource(SUM_URI);
		ClientResponse response = webResource.path(transaction_id)
				.accept("application/xml").get(ClientResponse.class);

		String result = response.getEntity(String.class);

		if (response.getStatus() == 200) {
			message = "GET" + " transactionservice/sum/" + transaction_id
					+ "\n" + result;
		} else {
			message = "GET failed status: " + response.getStatus();
		}
		return message;
	}

}
