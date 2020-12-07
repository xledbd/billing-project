package com.xledbd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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

	}

	public void changeSceneToUserInvoiceMenu(ActionEvent event) throws IOException {

	}

	public void changeSceneToUserTransactionMenu(ActionEvent event) throws IOException {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loginLabel.setText(App.user.getLogin());
		infoLabel.setText("Пользователь:");
	}
}
