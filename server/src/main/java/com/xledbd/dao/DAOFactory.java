package com.xledbd.dao;

import com.xledbd.entity.*;

public class DAOFactory {
	public static DAO<Contract> getContractDAO() { return new ContractDAO(); }
	public static DAO<Invoice> getInvoiceDAO() { return new InvoiceDAO(); }
	public static DAO<PriceHistory> getPriceHistoryDAO() { return new PriceHistoryDAO(); }
	public static DAO<Service> getServiceDAO() { return new ServiceDAO(); }
	public static DAO<Transaction> getTransactionDAO() { return new TransactionDAO(); }
	public static DAO<User> getUserDAO() { return new UserDAO(); }
}
