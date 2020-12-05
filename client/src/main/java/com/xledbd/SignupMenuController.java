package com.xledbd;

import com.xledbd.entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SignupMenuController implements Initializable {

	@FXML private TextField loginField;
	@FXML private TextField emailField;
	@FXML private PasswordField passwordField;
	@FXML private Label label;
	@FXML private Button signupButton;

	public void signupButtonPressed(ActionEvent event) throws Exception {
		String login = loginField.getText();
		String password = passwordField.getText();
		String email = emailField.getText();
		if (!(login.isEmpty() || password.isEmpty() || email.isEmpty()))
		{
			App.outputStream.writeObject("signup");
			String b;
			User user = new User(login, password);
			user.setEmail(email);
			App.outputStream.writeObject(user);
			b = (String)App.inputStream.readObject();
			if (b.equals("true"))
			{
				//successful sign up
				label.setText("Вы успешно зарегистрировались");
				label.setTextFill(Color.GREEN);
				label.setVisible(true);
			}
			else
			{
				label.setText("Ошибка регистрации. Проверьте введенные данные");
				label.setTextFill(Color.RED);
				label.setVisible(true);
			}
		}
	}

	public void changeSceneToLoginView(ActionEvent event) throws IOException {
		Stage window = (Stage)App.scene.getWindow();
		window.setTitle("Добро пожаловать");
		App.setRoot("loginMenu");
		window.setScene(App.scene);
		window.show();
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle)
	{
		loginField.focusedProperty().addListener((observableValue, aBoolean, t1) ->
		{
			if (t1) loginField.setStyle("-fx-control-inner-background: white;");
			else if (loginField.getText().isEmpty())
				loginField.setStyle("-fx-control-inner-background: #ff6f6f;");
		});
		emailField.focusedProperty().addListener((observableValue, aBoolean, t1) ->
		{
			if (t1) emailField.setStyle("-fx-control-inner-background: white;");
			else if (emailField.getText().isEmpty())
				emailField.setStyle("-fx-control-inner-background: #ff6f6f;");
		});
		passwordField.focusedProperty().addListener((observableValue, aBoolean, t1) ->
		{
			if (t1) passwordField.setStyle("-fx-control-inner-background: white;");
			else if (passwordField.getText().isEmpty())
				passwordField.setStyle("-fx-control-inner-background: #ff6f6f;");
		});
	}
}
