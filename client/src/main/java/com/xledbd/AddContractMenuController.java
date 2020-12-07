package com.xledbd;

import com.xledbd.entity.Contract;
import com.xledbd.entity.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddContractMenuController implements Initializable {

	@FXML private ListView<Service> listView;
	@FXML private TextArea textArea;
	@FXML private Label priceLabel;
	@FXML private VBox editVBox;
	@FXML private Label editLabel;

	public void changeSceneToMainMenu(ActionEvent event) throws IOException {
		Stage window = (Stage)App.scene.getWindow();
		window.setTitle("Главное меню");
		App.setRoot("clientMenu");
		window.setScene(App.scene);
		window.show();
	}

	public void listItemSelected(MouseEvent event) {
		Service service = listView.getSelectionModel().getSelectedItem();
		if (service != null) {
			editVBox.setVisible(true);
			textArea.setText(service.getDescription());
			priceLabel.setText("Цена: " + service.getPrice() + " руб./мес.");
			priceLabel.setVisible(true);
		}
	}

	public void addButtonPressed(ActionEvent event) throws Exception {
		Service service = listView.getSelectionModel().getSelectedItem();
		Contract contract = new Contract();
		contract.setService(service);
		contract.setUser(App.user);
		App.outputStream.writeObject("add_contract");
		App.outputStream.writeObject(contract);
		String b = (String) App.inputStream.readObject();
		if (b.equals("true")) {
			editLabel.setText("Услуга подключена");
			editLabel.setTextFill(Color.GREEN);
		} else {
			editLabel.setText("Ошибка при подключении услуги");
			editLabel.setTextFill(Color.RED);
		}
		editLabel.setVisible(true);
	}

	public void showPriceHistoryView(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader(App.class.getResource("priceHistoryView.fxml"));
		Parent parent = loader.load();

		Scene scene = new Scene(parent);
		Stage popupStage = new Stage();

		PriceHistoryViewController controller = loader.getController();
		controller.initData(listView.getSelectionModel().getSelectedItem());

		popupStage.initOwner(((Node)event.getSource()).getScene().getWindow());
		popupStage.initModality(Modality.WINDOW_MODAL);
		popupStage.setScene(scene);
		popupStage.showAndWait();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listView.setItems(ServiceMenuController.getServiceObservableList());
		priceLabel.setVisible(false);
		editVBox.setVisible(false);
	}
}
