package com.xledbd;

import com.xledbd.entity.User;

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

public class UserMenuController implements Initializable {
	@FXML private TableView<User> tableView;
	@FXML private TableColumn<User, String> loginColumn;
	@FXML private TableColumn<User, String> passwordColumn;
	@FXML private TableColumn<User, String> emailColumn;
	@FXML private TableColumn<User, LocalDate> regDateColumn;
	@FXML private TableColumn<User, String> accLevelStringColumn;
	@FXML private TableColumn<User, String> banStringColumn;

	@FXML private VBox editVBox;
	@FXML private TextField emailField;
	@FXML private TextField passwordField;
	@FXML private Button banButton;
	@FXML private Label editLabel;

	public void changeSceneToMainMenu(ActionEvent event) throws IOException {
		Stage window = (Stage)App.scene.getWindow();
		window.setTitle("Главное меню");
		App.setRoot("adminMenu");
		window.setScene(App.scene);
		window.show();
	}

	public void tableRowSelected(MouseEvent event)
	{
		User user = tableView.getSelectionModel().getSelectedItem();
		if (user != null)
		{
			editVBox.setVisible(true);
			editLabel.setVisible(false);
			emailField.setText(user.getEmail());
		//	passwordField.setText(user.getPassword());
			if (user.getBanStatus() == 0) banButton.setText("Заблокировать");
			else banButton.setText("Разблокировать");
		}
	}

	private void sendUserUpdate(User user) throws Exception {
		App.outputStream.writeObject("edit_user");
		App.outputStream.writeObject(user);
		String b = (String) App.inputStream.readObject();
		if (b.equals("true")) {
			tableView.setItems(getUserObservableList());
			editLabel.setText("Изменения сохранены");
			editLabel.setTextFill(Color.GREEN);
		}
		else {
			editLabel.setText("Ошибка при сохранении изменений");
			editLabel.setTextFill(Color.RED);
		}
		editLabel.setVisible(true);
	}

	public void editButtonPressed(ActionEvent event) throws Exception
	{
		User user = tableView.getSelectionModel().getSelectedItem();
		String email = emailField.getText();
		String password = passwordField.getText();
		if (!(email.isEmpty() || password.isEmpty())) {
			user.setEmail(email);
			user.setPassword(password);
			sendUserUpdate(user);
		} else {
			editLabel.setText("Поля не могут быть пустыми");
			editLabel.setTextFill(Color.RED);
			editLabel.setVisible(true);
		}
	}

	public void banButtonPressed(ActionEvent event) throws Exception
	{
		User user = tableView.getSelectionModel().getSelectedItem();
		if (user.getBanStatus() == 0) user.setBanStatus(1);
		else user.setBanStatus(0);
		sendUserUpdate(user);
	}

	public void changeAccessButtonPressed(ActionEvent event) throws Exception
	{
		User user = tableView.getSelectionModel().getSelectedItem();
		if (user.getAccessLevel() == 0) user.setAccessLevel(1);
		else user.setAccessLevel(0);
		sendUserUpdate(user);
	}

	public ObservableList<User> getUserObservableList()
	{
		List<User> list = null;
		try
		{
			App.outputStream.writeObject("get_users");
			list = (List<User>)App.inputStream.readObject();
		} catch (Exception e) { e.printStackTrace(); }
		for (User u: list) {
			u.setAccessLevel(u.getAccessLevel());
			u.setBanStatus(u.getBanStatus());
		}
		return FXCollections.observableArrayList(list);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
		passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		regDateColumn.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
		accLevelStringColumn.setCellValueFactory(new PropertyValueFactory<>("accessLevelString"));
		banStringColumn.setCellValueFactory(new PropertyValueFactory<>("banStatusString"));

		tableView.setItems(getUserObservableList());
		editVBox.setVisible(false);
	}
}
