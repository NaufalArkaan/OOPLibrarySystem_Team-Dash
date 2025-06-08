package controller.mahasiswa;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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

    private ObservableList<HistoryRecord> historyList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        historyTable.getColumns().clear();
        historyTable.getColumns().addAll(noCol, nameCol, classCol, titleCol, isbnCol, actionsCol);

        // Gunakan resize policy supaya kolom pas memenuhi lebar TableView
        historyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Atur kolom No. supaya tidak terlalu lebar dan rata tengah
        noCol.setMinWidth(40);
        noCol.setMaxWidth(60);
        noCol.setStyle("-fx-alignment: CENTER;");

        noCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(historyTable.getItems().indexOf(cellData.getValue()) + 1))
        );

        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        classCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getClassName()));
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        isbnCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIsbn()));

        addActionButtonsToTable();

        historyTable.setItems(historyList);

        // Contoh data dummy
        historyList.add(new HistoryRecord("Naufal Arkaan", "Informatika 2B", "Programming", "0000"));
        historyList.add(new HistoryRecord("Naufal Arkaan", "Informatika 2B", "Programming", "0000"));
    }

    private void addActionButtonsToTable() {
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");
            private final HBox buttonBox = new HBox(10, updateButton, deleteButton);

            {
                // Atur tombol dalam HBox supaya di tengah cell
                buttonBox.setAlignment(Pos.CENTER);

                updateButton.setOnAction(e -> {
                    HistoryRecord selected = getTableView().getItems().get(getIndex());
                    openEditWindow(selected);
                });

                deleteButton.setOnAction(e -> {
                    HistoryRecord selected = getTableView().getItems().get(getIndex());
                    historyList.remove(selected);
                });

                // Styling tombol (warna latar dan teks)
                updateButton.setStyle("-fx-background-color: #40c4c4; -fx-text-fill: white; -fx-background-radius: 10;");
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-background-radius: 10;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonBox);
                }
            }
        });
    }

    private void openEditWindow(HistoryRecord data) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/mahasiswa/EditForm.fxml"));
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
