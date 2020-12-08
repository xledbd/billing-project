package com.xledbd;

import com.xledbd.entity.Contract;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ClientContractMenuController implements Initializable {

	@FXML private TableView<Contract> tableView;
	@FXML private TableColumn<Contract, String> nameColumn;
	@FXML private TableColumn<Contract, String> priceColumn;
	@FXML private TableColumn<Contract, String> balanceColumn;
	@FXML private TableColumn<Contract, LocalDate> dateFromColumn;
	@FXML private TableColumn<Contract, LocalDate> dateToColumn;
	@FXML private TableColumn<Contract, String> statusColumn;

	@FXML private TextField balanceField;
	@FXML private Button disableButton;
	@FXML private Label editLabel;
	@FXML private VBox editVBox;

	public void changeSceneToMainMenu(ActionEvent event) throws IOException {
		Stage window = (Stage)App.scene.getWindow();
		window.setTitle("Главное меню");
		App.setRoot("clientMenu");
		window.setScene(App.scene);
		window.show();
	}

	public void tableRowSelected(MouseEvent event) {
		Contract contract = tableView.getSelectionModel().getSelectedItem();
		if (contract != null) {
			editVBox.setVisible(true);
			editLabel.setVisible(false);
			if (contract.getDateTo() != null) {
				disableButton.setText("Разблокировать");
			} else {
				disableButton.setText("Заблокировать");
			}
		}
	}

	private void sendContractUpdate(Contract contract) throws Exception {
		App.outputStream.writeObject("edit_contract");
		App.outputStream.writeObject(contract);
		String b = (String) App.inputStream.readObject();
		if (b.equals("true")) {
			tableView.setItems(getClientContractList());
			editLabel.setText("Изменения сохранены");
			editLabel.setTextFill(Color.GREEN);
		}
		else {
			editLabel.setText("Ошибка при сохранении изменений");
			editLabel.setTextFill(Color.RED);
		}
		editLabel.setVisible(true);
	}

	public void depositButtonPressed(ActionEvent event) throws Exception {
		try {
			double val = Double.parseDouble(balanceField.getText());
			if (val <= 0) throw new NumberFormatException();
			Contract contract = tableView.getSelectionModel().getSelectedItem();
			if (contract != null) {
				contract.setBalance(contract.getBalance() + val);
				sendContractUpdate(contract);
			}
		} catch (NumberFormatException e) {
			editLabel.setText("Введено неверное значение");
			editLabel.setTextFill(Color.RED);
		}
		editLabel.setVisible(true);
	}

	public void disableButtonPressed(ActionEvent event) throws Exception {
		Contract contract = tableView.getSelectionModel().getSelectedItem();
		if (contract.isActive()) {
			contract.setDateTo(LocalDate.now());
		} else {
			contract.setDateTo(null);
		}
		contract.setActive(!contract.isActive());
		sendContractUpdate(contract);
	}

	public void showContractTransactionHistoryView(ActionEvent event) throws Exception {

	}

	public ObservableList<Contract> getClientContractList() {
		List<Contract> list = null;
		try {
			App.outputStream.writeObject("get_client_contracts");
			App.outputStream.writeObject(App.user.getId());
			list = (List<Contract>) App.inputStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FXCollections.observableArrayList(list);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("serviceName"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("servicePrice"));
		balanceColumn.setCellValueFactory(new PropertyValueFactory<>("balance"));
		dateFromColumn.setCellValueFactory(new PropertyValueFactory<>("dateFrom"));
		dateToColumn.setCellValueFactory(new PropertyValueFactory<>("dateTo"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

		tableView.setItems(getClientContractList());
		editVBox.setVisible(false);
	}
}
