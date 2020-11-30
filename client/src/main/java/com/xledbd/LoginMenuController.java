package com.xledbd;

import com.xledbd.entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginMenuController {
	@FXML private TextField loginField;
	@FXML private PasswordField passwordField;
	@FXML private Label errorLabel;
	@FXML private Button signinButton;
	@FXML private Button signupButton;

	public void signinButtonPushed(ActionEvent event) throws Exception {
		String login = loginField.getText();
		String password = passwordField.getText();
		if (!(login.isEmpty() || password.isEmpty()))
		{
			App.outputStream.writeObject("signin");
			String b;
			App.outputStream.writeObject(new User(login, password));
			b = (String) App.inputStream.readObject();
			if (b.equals("true"))
			{
				//successful sign in
				App.user = (User) App.inputStream.readObject();
				errorLabel.setVisible(false);
				if (App.user.getBanStatus() != 0)
				{
					errorLabel.setText("Данный пользователь заблокирован");
					errorLabel.setTextFill(Color.RED);
					errorLabel.setVisible(true);
				}
				else
				{
					Stage window = (Stage)App.scene.getWindow();
					window.setTitle("Главное меню");
					if (App.user.getAccessLevel() == 1)
					{
						App.setRoot("adminMenu");
					}
					else
					{
						App.setRoot("clientMenu");
					}
					window.setScene(App.scene);
					window.show();
				}
			}
			else
			{
				errorLabel.setText("Неверная комбинация логин/пароль");
				errorLabel.setTextFill(Color.RED);
				errorLabel.setVisible(true);
			}
		}
	}

	public void changeSceneToSignupView(ActionEvent event) throws IOException {
		Stage window = (Stage)App.scene.getWindow();
		window.setTitle("Регистрация");
		App.setRoot("signup");
		window.setScene(App.scene);
		window.show();
	}
}
