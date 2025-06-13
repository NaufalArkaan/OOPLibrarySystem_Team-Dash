package controller.admin;

import controller.model.ReturnRecord;
import controller.model.SharedReturnData;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ReportsAndChartsController {

    @FXML private TableView<ReturnRecord> tableView;
    @FXML private TableColumn<ReturnRecord, Integer> colNo;
    @FXML private TableColumn<ReturnRecord, String> colStudentId;
    @FXML private TableColumn<ReturnRecord, String> colMemberName;
    @FXML private TableColumn<ReturnRecord, String> colBorrowDate;
    @FXML private TableColumn<ReturnRecord, String> colReturnDate;
    @FXML private TableColumn<ReturnRecord, Integer> colTotalBorrowed;
    @FXML private TableColumn<ReturnRecord, String> colFine;
    @FXML private TableColumn<ReturnRecord, String> colStatus;
    @FXML private TableColumn<ReturnRecord, Void> colActions;
    @FXML private TextField searchField;

    private ObservableList<ReturnRecord> reports;

    @FXML
    public void initialize() {
        reports = SharedReturnData.getReturnRecords();
        setupTableColumns();
        setupSearchField();
        tableView.setItems(reports);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupTableColumns() {
        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        centerAlignColumn(colNo);

        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        centerAlignColumn(colStudentId);

        colMemberName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        centerAlignColumn(colMemberName);

        colBorrowDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBorrowDateFormatted()));
        centerAlignColumn(colBorrowDate);

        colReturnDate.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReturnDateFormatted()));
        centerAlignColumn(colReturnDate);

        colTotalBorrowed.setCellValueFactory(new PropertyValueFactory<>("totalBorrowed"));
        centerAlignColumn(colTotalBorrowed);

        colFine.setCellValueFactory(new PropertyValueFactory<>("fine"));
        centerAlignColumn(colFine);

        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        centerAlignColumn(colStatus);

        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button addFineBtn = new Button();

            {
                ImageView icon = new ImageView("/view/images/pensil.png");
                icon.setFitHeight(16);
                icon.setFitWidth(16);
                addFineBtn.setGraphic(icon);
                addFineBtn.setStyle("-fx-background-color: transparent;");
                addFineBtn.setOnAction(event -> {
                    ReturnRecord report = getTableView().getItems().get(getIndex());
                    if (report != null) {
                        openAddFineDialog(report);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(addFineBtn);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });
    }

    private <T> void centerAlignColumn(TableColumn<ReturnRecord, T> column) {
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label label = new Label(item.toString());
                    label.setMaxWidth(Double.MAX_VALUE);
                    label.setAlignment(Pos.CENTER);
                    HBox box = new HBox(label);
                    box.setAlignment(Pos.CENTER);
                    box.setMaxWidth(Double.MAX_VALUE);
                    setGraphic(box);
                }
            }
        });
    }

    private void setupSearchField() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String keyword = newVal.toLowerCase().trim();
            if (keyword.isEmpty()) {
                tableView.setItems(reports);
            } else {
                ObservableList<ReturnRecord> filtered = reports.filtered(report ->
                        report.getMemberName().toLowerCase().contains(keyword) ||
                                report.getStudentId().toLowerCase().contains(keyword)
                );
                tableView.setItems(filtered);
            }
        });
    }

    private void openAddFineDialog(ReturnRecord report) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/AddFine.fxml"));
            Parent root = loader.load();

            AddFineController controller = loader.getController();
            if (controller != null) {
                controller.setData(report);
            }

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Fine");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            tableView.refresh();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Gagal membuka dialog Add Fine.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
