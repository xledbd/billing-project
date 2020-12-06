package com.xledbd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
	@FXML private Label infoLabel;
	@FXML private Label loginLabel;

	public void changeSceneToServiceMenu(ActionEvent event) throws IOException {
		Stage window = (Stage)App.scene.getWindow();
		window.setTitle("Управление услугами");
		App.setRoot("serviceMenu");
		window.setScene(App.scene);
		window.show();
	}

	public void changeSceneToUserMenu(ActionEvent event) throws IOException {
		Stage window = (Stage)App.scene.getWindow();
		window.setTitle("Управление пользователями");
		App.setRoot("userMenu");
		window.setScene(App.scene);
		window.show();
	}

	public void changeSceneToLoginView(ActionEvent event) throws IOException {
		Stage window = (Stage)App.scene.getWindow();
		window.setTitle("Добро пожаловать");
		App.setRoot("loginMenu");
		window.setScene(App.scene);
		window.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loginLabel.setText(App.user.getLogin());
		infoLabel.setText("Администратор:");
	}
}
