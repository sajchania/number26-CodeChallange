package com.number26.codechallange.main;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Creates the transaction service
 * 
 * @author Jacek Galewicz
 * 
 */
@Path("/transactionservice")
public class TransactionService {

	private static HashMap<Long, Transaction> transactions = new HashMap<Long, Transaction>();

	public HashMap<Long, Transaction> getTransactions() {
		return transactions;
	}

	public static final Logger logger = Logger.getLogger(Transaction.class
			.getCanonicalName());
	/**
	 * if the value of the parent_id is equal to 0, it is assumed that the
	 * parent_id wasn't given
	 * 
	 * @author Jacek Galewicz
	 */
	private static final long no_parent_id = 0;

	/**
	 * saves the Transaction object in a HashMap
	 * 
	 * @author Jacek Galewicz
	 * @param amount
	 * @param type
	 * @param transaction_id
	 * @param parent_id
	 * 
	 */
	private void saveTransaction(double amount, String type,
			long transaction_id, long parent_id) {
		Transaction transaction;
		if (parent_id != no_parent_id) {
			transaction = new Transaction(new Long(transaction_id), amount,
					type, parent_id);
		} else {
			transaction = new Transaction(new Long(transaction_id), amount,
					type, no_parent_id);
		}
		transactions.put(transaction.getTransaction_id(), transaction);
	}

	/**
	 * 
	 * @param amount
	 * @param type
	 * @param transaction_id
	 * @param parent_id
	 * @author Jacek Galewicz
	 * 
	 * @return converts the transaction object to JSON object(string in JSON
	 *         format)
	 */
	private static String transactionToJSON(double amount, String type,
			long transaction_id, long parent_id) {
		if (parent_id != no_parent_id) {
			return "{ \"amount\": " + amount + ", \"type\": " + type
					+ ", \"parent_id\": " + parent_id + " }"
					+ "\n=>{ \"status\": \"ok\" }";
		} else {
			return "{ \"amount\": " + amount + ", \"type\": " + type + " }"
					+ "\n=>{ \"status\": \"ok\" }";
		}
	}

	/**
	 * @author Jacek Galewicz
	 * @param t
	 * @return converts the transaction object to JSON object(string in JSON
	 *         format)
	 */
	private static String transactionToJSON(Transaction t) {
		return transactionToJSON(t.getAmount(), t.getType(),
				t.getTransaction_id(), t.getParent_id());

	}

	/**
	 * @author Jacek Galewicz
	 * @param transation
	 * @return created the transaction with the id given as a parameter
	 */
	@POST
	@Consumes("application/xml")
	@Path("/transaction")
	public String createTransaction(Transaction transaction) {
		return createTransaction(transaction.getAmount(),
				transaction.getType(), transaction.getTransaction_id(),
				transaction.getParent_id());
	}

	/**
	 * @author Jacek Galewicz
	 * @param amount
	 * @param type
	 * @param transation_id
	 * @return created the transaction with the id given as a parameter
	 */
	@POST
	@Consumes("application/xml")
	@Path("/transaction/{transaction_id}/{amount}/{type}")
	public String createTransaction(@PathParam("amount") double amount,
			@PathParam("type") String type,
			@PathParam("transaction_id") long transaction_id) {
		return createTransaction(amount, type, transaction_id, no_parent_id);
	}

	/**
	 * @author Jacek Galewicz
	 * @param amount
	 * @param type
	 * @param transation_id
	 * @param parent_id
	 * @return creates the transaction with the id given as a parameter
	 */
	@POST
	@Consumes("application/xml")
	@Path("/transaction/{transaction_id}/{amount}/{type}/{parent_id}")
	public String createTransaction(@PathParam("amount") double amount,
			@PathParam("type") String type,
			@PathParam("transaction_id") long transaction_id,
			@PathParam("parent_id") long parent_id) {
		String result = "";
		try {
			if (transaction_id == 0) {
				throw new Exception("zeroId!");
			}
			if (transactions.containsKey(new Long(transaction_id))) {
				throw new Exception("exists!");
			}
			saveTransaction(amount, type, transaction_id, parent_id);
			result += "=>{ \"status\": \"ok\" }";
			logger.log(Level.INFO, "Transaction with an id: {0} created",
					new Object[] { transaction_id });
		} catch (Exception e) {
			result += "{ }";
			if (e.getMessage().compareTo("zeroId!") == 0) {
				logger.log(Level.SEVERE,
						"Transaction can't have an id that is equal to 0");
			} else if (e.getMessage().compareTo("exists!") == 0) {
				logger.log(Level.SEVERE, "Transaction with an id: {0} exists",
						new Object[] { transaction_id });
			} else {
				throw new WebApplicationException(e,
						Response.Status.INTERNAL_SERVER_ERROR);
			}
		}
		return result;
	}

	/**
	 * @author Jacek Galewicz
	 * @param transaction
	 * @return updates the transaction with the id given as a parameter
	 */
	@PUT
	@Consumes("application/xml")
	@Path("/transaction")
	public String updateTransaction(Transaction transaction) {
		return updateTransaction(transaction.getAmount(),
				transaction.getType(), transaction.getTransaction_id(),
				transaction.getParent_id());
	}

	/**
	 * @author Jacek Galewicz
	 * @param amount
	 * @param type
	 * @param transation_id
	 * @return updates the transaction with the id given as a parameter
	 */
	@PUT
	@Consumes("application/xml")
	@Path("/transaction/{transaction_id}/{amount}/{type}")
	public String updateTransaction(@PathParam("amount") double amount,
			@PathParam("type") String type,
			@PathParam("transaction_id") long transaction_id) {
		return updateTransaction(amount, type, transaction_id, no_parent_id);
	}

	/**
	 * @author Jacek Galewicz
	 * @param amount
	 * @param type
	 * @param transation_id
	 * @param parent_id
	 * @return updates the transaction with the id given as a parameter
	 */
	@PUT
	@Consumes("application/xml")
	@Path("/transaction/{transaction_id}/{amount}/{type}/{parent_id}")
	public String updateTransaction(@PathParam("amount") double amount,
			@PathParam("type") String type,
			@PathParam("transaction_id") long transaction_id,
			@PathParam("parent_id") long parent_id) {
		String result = "";
		try {
			if (!transactions.containsKey(new Long(transaction_id))) {
				throw new Exception("Does not exists!");
			}
			saveTransaction(amount, type, transaction_id, parent_id);
			result += "=>{ \"status\": \"ok\" }";
		} catch (Exception e) {
			result += "{ }";
			if (e.getMessage().compareTo("Does not exists!") == 0) {
				logger.log(Level.SEVERE,
						"Transaction with an id: {0} does not exists",
						new Object[] { transaction_id });
			} else {
				throw new WebApplicationException(e,
						Response.Status.INTERNAL_SERVER_ERROR);
			}
		}
		return result;
	}

	/**
	 * @author Jacek Galewicz
	 * @param id
	 * @return the transaction with the id given as a parameter
	 */
	@GET
	@Path("transaction/{id}")
	@Produces({ "application/xml", "application/json" })
	public String getTransaction(@PathParam("id") String transactionId) {
		Long transaction_id = new Long(transactionId);
		String result = "";
		try {
			Transaction transaction = transactions.get(transaction_id);
			result = transactionToJSON(transaction);
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Transaction with an id: {0} was not found",
					new Object[] { transactionId });
			result = "{ }";
		}
		return result;
	}

	/**
	 * @author Jacek Galewicz
	 * @param type
	 * @return transaction Id's which have the type given in the parameter
	 * @throws Exception
	 */
	@GET
	@Path("types/{type}")
	@Produces({ "application/xml", "application/json" })
	public String getType(@PathParam("type") String type) {
		String result = "=> [ ";
		try {
			HashMap<Long, Transaction> typeTransactions = findByType(type);
			if (typeTransactions.size() == 0) {
				throw new Exception("There are no transactions with this type");
			}

			for (Entry<Long, Transaction> element : typeTransactions.entrySet()) {
				result += element.getKey().toString() + ", ";
			}
			// remove last ", "
			result = result.substring(0, result.length() - 2);
			result += " ]";
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"There are no transactions with the type: {0}. ",
					new Object[] { type });
			result += " ]";
		}
		return result;
	}

	/**
	 * @author Jacek Galewicz
	 * @param type
	 * @return computation of the {@link TransactionService#getType(String)}
	 */
	private HashMap<Long, Transaction> findByType(String type) {
		HashMap<Long, Transaction> typeTransaction = new HashMap<Long, Transaction>();
		for (Entry<Long, Transaction> element : transactions.entrySet()) {
			if ((element.getValue().getType()).compareTo(type) == 0) {
				typeTransaction.put(element.getKey(), element.getValue());
			}
		}
		return typeTransaction;
	}

	/**
	 * @author Jacek Galewicz
	 * @param transactionId
	 * @return sum of all transactions which have a parent_id equal to the id of
	 *         the transaction (specified by transactionId) with the value of
	 *         the transaction in JSON format.
	 */
	@GET
	@Path("sum/{id}")
	@Produces({ "application/xml", "application/json" })
	public String getSum(@PathParam("id") String transactionId) {
		Long transaction_id = new Long(transactionId);
		String result = "=> { \"sum \": ";
		double sum = sumByParent(transaction_id);
		try {
			sum += transactions.get(transaction_id).getAmount();
			result += sum + " }";
		} catch (NullPointerException e) {
			logger.log(Level.SEVERE,
					"There is no transaction with transactionId {0}.",
					new Object[] { transactionId });
			result += " }";
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unknown Exception Occured: {0}.",
					new Object[] { e.getMessage() });
			result += " }";
		}
		return result;
	}

	/**
	 * @author Jacek Galewicz
	 * @param transactionId
	 * @return computation of the {@link TransactionService#getSum(String)}
	 */
	private double sumByParent(long transactionId) {
		double result = 0;
		for (Entry<Long, Transaction> element : transactions.entrySet()) {
			if (element.getValue().getParent_id() == transactionId
					&& element.getValue().getTransaction_id() != transactionId) {
				result += element.getValue().getAmount();
			}
		}
		return result;
	}

	/**
	 * @author Jacek Galewicz
	 * @param transactionId
	 * @return sum of all transactions which have a parent_id equal to the id of
	 *         the transaction (specified by transactionId) with the value of
	 *         the transaction in JSON format.
	 */
	@DELETE
	@Path("delete")
	@Produces({ "application/xml", "application/json" })
	public String deleteTransactions() {
		try {
			transactions = new HashMap<Long, Transaction>();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unknown Exception Occured: {0}.",
					new Object[] { e.getMessage() });
		}
		return "test";
	}

}
