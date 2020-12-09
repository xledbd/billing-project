package com.xledbd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {
	@FXML private Label infoLabel;
	@FXML private Label loginLabel;

	public void changeSceneToLoginView(ActionEvent event) throws IOException {
		Stage window = (Stage)App.scene.getWindow();
		window.setTitle("Добро пожаловать");
		App.setRoot("loginMenu");
		window.setScene(App.scene);
		window.show();
	}

	public void changeSceneToAddContractMenu(ActionEvent event) throws IOException {
		Stage window = (Stage)App.scene.getWindow();
		window.setTitle("Выбор услуги");
		App.setRoot("addContractMenu");
		window.setScene(App.scene);
		window.show();
	}

	public void changeSceneToUserContractMenu(ActionEvent event) throws IOException {
		Stage window = (Stage)App.scene.getWindow();
		window.setTitle("Список услуг");
		App.setRoot("clientContractMenu");
		window.setScene(App.scene);
		window.show();
	}

	public void changeSceneToUserInvoiceMenu(ActionEvent event) throws IOException {

	}

	public void changeSceneToUserTransactionMenu(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(App.class.getResource("transactionHistoryView.fxml"));
		Parent parent = loader.load();

		Scene scene = new Scene(parent);
		Stage popupStage = new Stage();

		TransactionHistoryViewController controller = loader.getController();
		controller.initData(null);

		popupStage.initOwner(((Node)event.getSource()).getScene().getWindow());
		popupStage.initModality(Modality.WINDOW_MODAL);
		popupStage.setScene(scene);
		popupStage.showAndWait();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loginLabel.setText(App.user.getLogin());
		infoLabel.setText("Пользователь:");
	}
}
