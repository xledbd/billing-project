package com.xledbd;

import com.xledbd.entity.Invoice;
import com.xledbd.entity.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.poi.xwpf.usermodel.*;

public class InvoiceMenuController implements Initializable {

	@FXML private TableView<Invoice> tableView;
	@FXML private TableColumn<Invoice, String> idColumn;
	@FXML private TableColumn<Invoice, String> serviceColumn;
	@FXML private TableColumn<Invoice, String> amountColumn;
	@FXML private TableColumn<Invoice, LocalDateTime> dateColumn;
	@FXML private TableColumn<Invoice, String> transactionColumn;
	@FXML private TableColumn<Invoice, String> resultColumn;

	public void exportButtonPressed(ActionEvent event) throws Exception	{
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Выберите папку");

		String selectedPath = directoryChooser.showDialog(App.scene.getWindow()).getAbsolutePath();

		List<Invoice> list = tableView.getItems();

		XWPFDocument document = new XWPFDocument();
		FileOutputStream out = new FileOutputStream(new File(selectedPath + "\\invoice_report.docx"));

		XWPFParagraph paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun run = paragraph.createRun();
		run.setBold(true);
		run.setFontFamily("Times New Roman");
		run.setFontSize(14);
		run.setText("Список выставленных счетов");
		run.addCarriageReturn();

		int failedTries = 0;
		for (Invoice invoice: list) {
			if (!invoice.isResult())
				failedTries++;
		}

		paragraph = document.createParagraph();
		paragraph.setAlignment(ParagraphAlignment.LEFT);
		run = paragraph.createRun();
		run.setFontFamily("Times New Roman");
		run.setFontSize(14);
		run.setText("Количество выставленных счетов: " + list.size());
		run.addCarriageReturn();
		run.setText("Количество неуспешных оплат: " + failedTries);
		run.addCarriageReturn();

		XWPFTable table = document.createTable(list.size() + 1, 6);
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
		row.getCell(1).setText("Услуга");
		row.getCell(2).setText("Сумма");
		row.getCell(3).setText("Дата");
		row.getCell(4).setText("Номер транзакции");
		row.getCell(5).setText("Результат");
		for (int i = 0; i < list.size(); i++)
		{
			row = table.getRow(i + 1);
			Invoice invoice = list.get(i);
			row.getCell(0).setText(Integer.toString(invoice.getId()));
			row.getCell(1).setText(invoice.getService());
			row.getCell(2).setText(String.format("%.2f", invoice.getAmount()));
			row.getCell(3).setText(invoice.getTdate().toString());
			row.getCell(4).setText(invoice.getTransactionId());
			row.getCell(5).setText(invoice.getResultString());
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

		Desktop.getDesktop().open(new File(selectedPath + "\\invoice_report.docx"));
	}

	public void changeSceneToMainMenu(ActionEvent event) throws IOException {
		Stage window = (Stage)App.scene.getWindow();
		window.setTitle("Главное меню");
		App.setRoot("clientMenu");
		window.setScene(App.scene);
		window.show();
	}

	public ObservableList<Invoice> getClientInvoiceList() {
		List<Invoice> list = null;
		try {
			App.outputStream.writeObject("get_client_invoices");
			App.outputStream.writeObject(App.user.getId());
			list = (List<Invoice>) App.inputStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FXCollections.observableArrayList(list);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		serviceColumn.setCellValueFactory(new PropertyValueFactory<>("service"));
		amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("tdate"));
		transactionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
		resultColumn.setCellValueFactory(new PropertyValueFactory<>("resultString"));

		tableView.setItems(getClientInvoiceList());
	}
}
