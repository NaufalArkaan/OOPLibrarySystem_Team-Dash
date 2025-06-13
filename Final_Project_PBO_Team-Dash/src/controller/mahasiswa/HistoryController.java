package controller.mahasiswa;

import controller.model.HistoryRecord;
import controller.model.SharedHistoryData;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HistoryController {

    @FXML private TableView<HistoryRecord> historyTable;
    @FXML private TableColumn<HistoryRecord, String> noCol;
    @FXML private TableColumn<HistoryRecord, String> nameCol;
    @FXML private TableColumn<HistoryRecord, String> classCol;
    @FXML private TableColumn<HistoryRecord, String> titleCol;
    @FXML private TableColumn<HistoryRecord, String> isbnCol;
    @FXML private TableColumn<HistoryRecord, Void> actionsCol;

    private ObservableList<HistoryRecord> historyList;

    @FXML
    public void initialize() {
        // Ambil data dari SharedHistoryData
        historyList = SharedHistoryData.getHistoryRecords(); // ✅ perbaiki method ini

        historyTable.getColumns().clear();
        historyTable.getColumns().addAll(noCol, nameCol, classCol, titleCol, isbnCol, actionsCol);
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Konfigurasi kolom
        noCol.setMinWidth(40);
        noCol.setMaxWidth(60);
        noCol.setStyle("-fx-alignment: CENTER;");
        noCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(historyTable.getItems().indexOf(cellData.getValue()) + 1)));

        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        classCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getClassName()));
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        isbnCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIsbn()));

        addActionButtonsToTable();

        // Set data ke tabel
        historyTable.setItems(historyList);
    }

    private void addActionButtonsToTable() {
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonBox = new HBox(10, updateButton, deleteButton);

            {
                buttonBox.setAlignment(Pos.CENTER);

                updateButton.setOnAction(e -> {
                    HistoryRecord selected = getTableView().getItems().get(getIndex());
                    openEditWindow(selected);
                });

                deleteButton.setOnAction(e -> {
                    HistoryRecord selected = getTableView().getItems().get(getIndex());
                    SharedHistoryData.removeHistoryRecord(selected);
                });

                updateButton.setStyle("-fx-background-color: #40c4c4; -fx-text-fill: white; -fx-background-radius: 10;");
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 10;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonBox);
            }
        });
    }

    private void openEditWindow(HistoryRecord data) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mahasiswa/EditForm.fxml")); // ✅ perbaiki path jika perlu
            Parent root = loader.load();

            EditDataController controller = loader.getController();
            controller.setData(data);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Data");
            stage.show();

            stage.setOnHiding(event -> historyTable.refresh());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
