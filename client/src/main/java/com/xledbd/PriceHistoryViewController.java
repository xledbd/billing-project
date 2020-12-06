package com.xledbd;

import com.xledbd.entity.PriceHistory;
import com.xledbd.entity.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;


public class PriceHistoryViewController {

	private Service service;

	@FXML private TableView<PriceHistory> tableView;
	@FXML private TableColumn<PriceHistory, String> dateFromColumn;
	@FXML private TableColumn<PriceHistory, String> dateToColumn;
	@FXML private TableColumn<PriceHistory, String> priceColumn;

	@FXML private BarChart<CategoryAxis, NumberAxis> barChart;
	@FXML private LineChart<NumberAxis, NumberAxis> lineChart;

	public ObservableList<PriceHistory> getPriceHistoryObservableList() {
		List<PriceHistory> list = null;
		try {
			App.outputStream.writeObject("get_history");
			App.outputStream.writeObject(service.getId());
			list = (List<PriceHistory>) App.inputStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FXCollections.observableArrayList(list);
	}

	public void initData(Service service) {
		this.service = service;

		dateFromColumn.setCellValueFactory(new PropertyValueFactory<>("dateFrom"));
		dateToColumn.setCellValueFactory(new PropertyValueFactory<>("dateTo"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

		tableView.setItems(getPriceHistoryObservableList());

		barChart.getXAxis().setLabel("Дата");
		barChart.getYAxis().setLabel("Цена");

		XYChart.Series dataSeries = new XYChart.Series<>();
		dataSeries.setName(service.getName());

		for (PriceHistory p: tableView.getItems()) {
			dataSeries.getData().add(new XYChart.Data<>(p.getDateFrom().toString(), p.getPrice()));
		}

		barChart.getData().add(dataSeries);

		lineChart.getXAxis().setLabel("Количество дней");
		lineChart.getYAxis().setLabel("Цена");

		XYChart.Series lineSeries = new XYChart.Series();
		lineSeries.setName(service.getName());

		long offset = tableView.getItems().get(0).getDateFrom().toEpochDay();

		for (PriceHistory p: tableView.getItems()) {
			lineSeries.getData().add(new XYChart.Data<>(p.getDateFrom().toEpochDay() - offset, p.getPrice()));
			if (p.getDateTo() != null)
				lineSeries.getData().add(new XYChart.Data<>(p.getDateTo().toEpochDay() - offset, p.getPrice()));
			else
				lineSeries.getData().add(new XYChart.Data<>(LocalDate.now().toEpochDay() - offset, p.getPrice()));
		}

		lineChart.getData().add(lineSeries);
	}
}
