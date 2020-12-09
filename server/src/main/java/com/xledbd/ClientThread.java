package com.xledbd;

import com.xledbd.dao.DAOFactory;
import com.xledbd.entity.*;
import org.hibernate.JDBCException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class ClientThread implements Runnable {

	private final Socket socket;
	private ObjectInputStream inputStream = null;
	private ObjectOutputStream outputStream = null;

	public ClientThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		String msg;
		try {
			App.print_log("New user connected...");
			inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			msg = (String)inputStream.readObject();
			while (!"exit".equals(msg)) {
				switch (msg) {
					case "signin" -> signin();
					case "signup" -> signup();
					case "add_service" -> addService();
					case "add_contract" -> addContract();
					case "edit_user" -> editUser();
					case "edit_service" -> editService();
					case "edit_contract" -> editContract();
					case "remove_service" -> removeService();
					case "get_users" -> getUsers();
					case "get_services" -> getServices();
					case "get_history" -> getHistory();
					case "get_transactions" -> getTransactions();
					case "get_client_contracts" -> getClientContracts();
					case "get_client_invoices" -> getClientInvoices();
					case "get_contract_transactions" -> getContractTransactions();
				}
				msg = (String)inputStream.readObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			App.print_log("User disconnected...");
		}
	}

	private void addService() throws Exception {
		App.print_log("Trying to add service...");
		Service service = (Service) inputStream.readObject();
		int res = -1;
		try {
			res = DAOFactory.getServiceDAO().create(service);
		} catch (JDBCException e) {
			e.printStackTrace();
		}
		if (res != -1) {
			App.print_log("Service added...");
			outputStream.writeObject("true");
		} else {
			App.print_log("Add service failed...");
			outputStream.writeObject("false");
		}
	}

	public void addContract() throws Exception {
		App.print_log("Trying to add contract...");
		Contract contract = (Contract) inputStream.readObject();
		int res = -1;
		try {
			res = DAOFactory.getContractDAO().create(contract);
		} catch (JDBCException e) {
			e.printStackTrace();
		}
		if (res != -1) {
			App.print_log("Contract added...");
			outputStream.writeObject("true");
		} else {
			App.print_log("Add contract failed...");
			outputStream.writeObject("false");
		}
	}

	private void editUser() throws Exception {
		User user = (User) inputStream.readObject();
		App.print_log("Trying to update user " + user.getLogin() + "...");
		boolean b = false;
		try {
			DAOFactory.getUserDAO().save(user);
			b = true;
		} catch (JDBCException e) {
			e.printStackTrace();
		}
		if (b) {
			App.print_log("User successfully updated...");
			outputStream.writeObject("true");
		}
		else {
			App.print_log("Update failed...");
			outputStream.writeObject("false");
		}
	}

	private void editService() throws Exception {
		Service service = (Service) inputStream.readObject();
		App.print_log("Trying to update service " + service.getName() + "...");
		boolean b = false;
		try {
			DAOFactory.getServiceDAO().save(service);
			b = true;
		} catch (JDBCException e) {
			e.printStackTrace();
		}
		if (b) {
			App.print_log("Service successfully updated...");
			outputStream.writeObject("true");
		}
		else {
			App.print_log("Update service failed...");
			outputStream.writeObject("false");
		}
	}

	private void editContract() throws Exception {
		Contract contract = (Contract) inputStream.readObject();
		App.print_log("Trying to update contract " + contract.getId() + "...");
		boolean b = false;
		try {
			DAOFactory.getContractDAO().save(contract);
			b = true;
		} catch (JDBCException e) {
			e.printStackTrace();
		}
		if (b) {
			App.print_log("Contract successfully updated...");
			outputStream.writeObject("true");
		}
		else {
			App.print_log("Update contract failed...");
			outputStream.writeObject("false");
		}
	}

	private void removeService() throws Exception {
		int id = (int) inputStream.readObject();
		App.print_log("Trying to delete service #" + id + "...");
		boolean b = false;
		try {
			DAOFactory.getServiceDAO().delete(id);
			b = true;
		} catch (JDBCException e) {
			e.printStackTrace();
		}
		if (b) {
			App.print_log("Service successfully deleted...");
			outputStream.writeObject("true");
		}
		else {
			App.print_log("Delete service failed...");
			outputStream.writeObject("false");
		}
	}

	private void getUsers() throws Exception {
		App.print_log("Getting list of users...");
		List<User> list = DAOFactory.getUserDAO().getList();
		App.print_log("Sending list to client...");
		outputStream.writeObject(list);
	}

	private void getServices() throws Exception {
		App.print_log("Getting list of services...");
		List<Service> list = DAOFactory.getServiceDAO().getList();
		App.print_log("Sending list to client...");
		outputStream.writeObject(list);
	}

	private void getHistory() throws Exception {
		int id = (Integer) inputStream.readObject();
		App.print_log("Getting price history of service #" + id + "...");
		List<PriceHistory> list =
				DAOFactory.getPriceHistoryDAO().getList()
						.stream().filter(item -> item.getService().getId() == id)
						.collect(Collectors.toList());
		App.print_log("Sending list to client...");
		outputStream.writeObject(list);
	}

	private void getTransactions() throws Exception {
		App.print_log("Getting list of transactions...");
		List<Transaction> list = DAOFactory.getTransactionDAO().getList();
		App.print_log("Sending list to client...");
		outputStream.writeObject(list);
	}

	private void getClientContracts() throws Exception {
		int id = (int) inputStream.readObject();
		App.print_log("Getting list of contracts for user " + id +"...");
		List<Contract> list =
				DAOFactory.getContractDAO().getList()
					.stream().filter(item -> item.getUser().getId() == id)
					.collect(Collectors.toList());
		App.print_log("Sending list to client...");
		outputStream.writeObject(list);
	}

	private void getClientInvoices() throws Exception {
		int id = (int) inputStream.readObject();
		App.print_log("Getting list of invoices for user " + id +"...");
		List<Invoice> list =
				DAOFactory.getInvoiceDAO().getList()
				.stream().filter(item -> item.getContract().getUser().getId() == id)
				.collect(Collectors.toList());
		App.print_log("Sending list to client...");
		outputStream.writeObject(list);
	}

	private void getContractTransactions() throws Exception {
		int id = (int) inputStream.readObject();
		int userId = (int) inputStream.readObject();
		App.print_log("Getting list of transactions for contract " + id +"...");
		List<Transaction> list;
		if (id != 0) {
			list = DAOFactory.getTransactionDAO().getList()
					.stream().filter(item -> {
						if (item.getContractFrom() == null) {
							return item.getContractTo().getId() == id;
						} else
							return item.getContractFrom().getId() == id;
					}).collect(Collectors.toList());
		}
		else {
			list = DAOFactory.getTransactionDAO().getList();
			if (DAOFactory.getUserDAO().get(userId).getAccessLevel() == 0) {
				list = list.stream().filter(item -> {
					if (item.getContractFrom() == null) {
						return item.getContractTo().getUser().getId() == userId;
					} else
						return item.getContractFrom().getUser().getId() == userId;
				}).collect(Collectors.toList());
			}
		}
		App.print_log("Sending list to client...");
		outputStream.writeObject(list);
	}

	private void signup() throws Exception {
		User user = (User)inputStream.readObject();
		App.print_log("Trying to sign up as " + user.getLogin() + "...");
		User ret = null;
		int res = -1;
		try {
			res = DAOFactory.getUserDAO().create(user);
		} catch (JDBCException e) {
			e.printStackTrace();
		}
		if (res != -1) {
			App.print_log("Sign up successful...");
			outputStream.writeObject("true");
		}
		else {
			App.print_log("Sign up failed...");
			outputStream.writeObject("false");
		}
	}

	private void signin() throws Exception {
		User user = (User)inputStream.readObject();
		App.print_log("Trying to sign in as " + user.getLogin() + "...");
		User compare = null;
		boolean res = false;
		try {
			List<User> list = DAOFactory.getUserDAO().getList();
			for (User u: list) {
				if (compare != null) break;
				if (u.getLogin().equals(user.getLogin()))
					compare = u;
			}
			if (compare != null) {
				user.setBanStatus(compare.getBanStatus());
				user.setAccessLevel(compare.getAccessLevel());
				res = user.getPassword().equals(compare.getPassword());
			}
		} catch (JDBCException e) {
			e.printStackTrace();
		}
		if (res) {
			App.print_log("Sign in successful...");
			outputStream.writeObject("true");
			outputStream.writeObject(compare);
		}
		else {
			App.print_log("Sign in failed...");
			outputStream.writeObject("false");
		}
	}
}
