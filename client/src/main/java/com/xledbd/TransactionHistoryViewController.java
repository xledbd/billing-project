package com.xledbd;

import com.xledbd.entity.Contract;
import com.xledbd.entity.Transaction;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import org.apache.poi.xwpf.usermodel.*;

public class TransactionHistoryViewController {

	private Contract contract;

	@FXML private TableView<Transaction> tableView;
	@FXML private TableColumn<Transaction, String> idColumn;
	@FXML private TableColumn<Transaction, String> typeColumn;
	@FXML private TableColumn<Transaction, String> serviceColumn;
	@FXML private TableColumn<Transaction, String> amountColumn;
	@FXML private TableColumn<Transaction, LocalDateTime> dateColumn;

	@FXML private BarChart<CategoryAxis, NumberAxis> barChart;

	@FXML private VBox statsVBox;
	@FXML private PieChart depositPieChart;
	@FXML private PieChart invoicePieChart;

	public void exportButtonPressed(ActionEvent event) throws Exception {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Выберите папку");

		String selectedPath = directoryChooser.showDialog(App.scene.getWindow()).getAbsolutePath();

		List<Transaction> list = tableView.getItems();

		XWPFDocument document = new XWPFDocument();
		FileOutputStream out = new FileOutputStream(new File(selectedPath + "\\transaction_report.docx"));

		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun run = paragraph.createRun();
		run.setBold(true);
		run.setFontFamily("Times New Roman");
		run.setFontSize(14);
		run.setText("Список транзакций");
		run.addCarriageReturn();

		XWPFTable table = document.createTable(list.size() + 1, 5);
		table.setTableAlignment(TableRowAlign.CENTER);
		XWPFTableRow row;

		for (int x = 0; x < table.getNumberOfRows(); x++)
		{
			row = table.getRow(x);
			int numberOfCell = row.getTableCells().size();
			for (int y = 0; y < numberOfCell ; y++)
			{
				XWPFTableCell cell = row.getCell(y);
				cell.removeParagraph(0);
				paragraph = cell.addParagraph();
				paragraph.setAlignment(ParagraphAlignment.CENTER);
				run = paragraph.createRun();
				run.setFontFamily("Times New Roman");
				run.setFontSize(14);
				if (x == 0) run.setBold(true);
				cell.getCTTc().addNewTcPr().addNewTcW().setW(BigInteger.valueOf(20000));
			}
		}
		row = table.getRow(0);
		row.getCell(0).setText("Номер");
		row.getCell(1).setText("Тип операции");
		row.getCell(2).setText("Услуга");
		row.getCell(3).setText("Сумма");
		row.getCell(4).setText("Дата");
		for (int i = 0; i < list.size(); i++)
		{
			row = table.getRow(i + 1);
			Transaction transaction = list.get(i);
			row.getCell(0).setText(Integer.toString(transaction.getId()));
			row.getCell(1).setText(transaction.getType());
			row.getCell(2).setText(transaction.getService());
			row.getCell(3).setText(String.format("%.2f", transaction.getAmount()));
			row.getCell(4).setText(transaction.getDate().toString());
		}

		paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.LEFT);
		run = paragraph.createRun();
		run.setFontFamily("Times New Roman");
		run.setFontSize(14);

		run.addCarriageReturn();

		run.setText("Дата создания отчета:");
		run.addCarriageReturn();
		run.setText(LocalDate.now().toString());
		run.addCarriageReturn();

		document.write(out);
		out.close();

		Desktop.getDesktop().open(new File(selectedPath + "\\transaction_report.docx"));

	}

	public ObservableList<Transaction> getContractTransactionList() {
		List<Transaction> list = null;
		try {
			App.outputStream.writeObject("get_contract_transactions");
			App.outputStream.writeObject(contract.getId());
			App.outputStream.writeObject(App.user.getId());
			list = (List<Transaction>) App.inputStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FXCollections.observableArrayList(list);
	}

	public void initData(Contract contract) throws Exception {
		if (contract != null)
			this.contract = contract;
		else {
			this.contract = new Contract();
			this.contract.setId(0);
		}

		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
		serviceColumn.setCellValueFactory(new PropertyValueFactory<>("service"));
		amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

		tableView.setItems(getContractTransactionList());

		barChart.getXAxis().setLabel("Дата");
		barChart.getYAxis().setLabel("Сумма");

		XYChart.Series depositSeries = new XYChart.Series();
		depositSeries.setName("Пополнение");

		XYChart.Series invoiceSeries = new XYChart.Series();
		invoiceSeries.setName("Оплата");

		TreeMap<LocalDate, Double> depositMap = new TreeMap<>((o1, o2) -> {
			long e1 = o1.toEpochDay();
			long e2 = o2.toEpochDay();
			return Long.compare(e1, e2);
		});
		TreeMap<LocalDate, Double> invoiceMap = new TreeMap<>((o1, o2) -> {
			long e1 = o1.toEpochDay();
			long e2 = o2.toEpochDay();
			return Long.compare(e1, e2);
		});

		for (Transaction t: tableView.getItems()) {
			if ("Пополнение счета".equals(t.getType())) {
				depositMap.merge(t.getDate().toLocalDate(), t.getAmount(), Double::sum);
				invoiceMap.merge(t.getDate().toLocalDate(), 0., Double::sum);
			}
			else {
				invoiceMap.merge(t.getDate().toLocalDate(), t.getAmount(), Double::sum);
				depositMap.merge(t.getDate().toLocalDate(), 0., Double::sum);
			}
		}
		depositMap.forEach((key, val) -> depositSeries.getData().add(new XYChart.Data<>(key.toString(), val)));
		invoiceMap.forEach((key, val) -> invoiceSeries.getData().add(new XYChart.Data<>(key.toString(), val)));

		barChart.getData().add(depositSeries);
		barChart.getData().add(invoiceSeries);

		if (this.contract.getId() == 0) {
			statsVBox.setVisible(true);
			HashMap<String, Double> depositHash = new HashMap<>();
			HashMap<String, Double> invoiceHash = new HashMap<>();
			for (Transaction t : tableView.getItems()) {
				if ("Пополнение счета".equals(t.getType())) {
					depositHash.merge(t.getService(), t.getAmount(), Double::sum);
				} else {
					invoiceHash.merge(t.getService(), t.getAmount(), Double::sum);
				}
			}
			depositHash.forEach((key, val) -> depositPieChart.getData().add(new PieChart.Data(key, val)));
			invoiceHash.forEach((key, val) -> invoicePieChart.getData().add(new PieChart.Data(key, val)));
			depositPieChart.setTitle("Пополнения");
			invoicePieChart.setTitle("Оплаты");

			depositPieChart.getData().forEach(data ->
					data.nameProperty().bind(
							Bindings.concat(
									data.getName(), ", ", data.pieValueProperty(), " руб."
							)
					)
			);
			invoicePieChart.getData().forEach(data ->
					data.nameProperty().bind(
							Bindings.concat(
									data.getName(), ", ", data.pieValueProperty(), " руб."
							)
					)
			);
		}
		else
			statsVBox.setVisible(false);
	}
}
