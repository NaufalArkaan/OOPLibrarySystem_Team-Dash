package controller.admin;

import controller.model.ReturnRecord;
import controller.model.SharedReturnData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class ReturnBooksController {

    @FXML private TableView<ReturnRecord> tableView;
    @FXML private TableColumn<ReturnRecord, Integer> colNo;
    @FXML private TableColumn<ReturnRecord, String> colStudentId;
    @FXML private TableColumn<ReturnRecord, String> colMemberName;
    @FXML private TableColumn<ReturnRecord, String> colBookTitle;
    @FXML private TableColumn<ReturnRecord, String> colBorrowDate;  // ✅ ubah ke String
    @FXML private TableColumn<ReturnRecord, String> colReturnDate;  // ✅ ubah ke String
    @FXML private TableColumn<ReturnRecord, String> colFine;
    @FXML private TableColumn<ReturnRecord, Void> colAction;
    @FXML private TextField searchField;

    private final ObservableList<ReturnRecord> returnList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initTableColumns();
        addReturnButtonToTable();
        setupSearchFilter();
        refreshTable();

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void initTableColumns() {
        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colMemberName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        colBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDateFormatted"));   // ✅
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDateFormatted"));   // ✅
        colFine.setCellValueFactory(new PropertyValueFactory<>("fine"));

        // Align all columns to center
        String centerStyle = "-fx-alignment: CENTER;";
        colNo.setStyle(centerStyle);
        colStudentId.setStyle(centerStyle);
        colMemberName.setStyle(centerStyle);
        colBookTitle.setStyle(centerStyle);
        colBorrowDate.setStyle(centerStyle);
        colReturnDate.setStyle(centerStyle);
        colFine.setStyle(centerStyle);
    }

    private void addReturnButtonToTable() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button returnBtn = new Button("📄 Return");

            {
                returnBtn.setStyle("-fx-background-color: #C3F5D5; -fx-text-fill: black; -fx-background-radius: 8;");
                returnBtn.setOnAction(event -> {
                    ReturnRecord record = getTableView().getItems().get(getIndex());
                    record.setStatus("Sudah Kembali");
                    record.setReturnDate(LocalDate.now());

                    refreshTable();
                    showInfoAlert("Return Success", "Book \"" + record.getBookTitle() + "\" has been returned.");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(returnBtn) {{
                    setAlignment(Pos.CENTER);
                }});
            }
        });
    }

    private void setupSearchFilter() {
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String keyword = newVal.toLowerCase();
            ObservableList<ReturnRecord> filtered = returnList.filtered(record ->
                    record.getStudentId().toLowerCase().contains(keyword) ||
                            record.getMemberName().toLowerCase().contains(keyword) ||
                            record.getBookTitle().toLowerCase().contains(keyword)
            );
            tableView.setItems(filtered);
        });
    }

    private void refreshTable() {
        returnList.setAll(
                SharedReturnData.getReturnRecords().stream()
                        .filter(record -> "Belum Kembali".equalsIgnoreCase(record.getStatus()))
                        .collect(Collectors.toList())
        );
        tableView.setItems(returnList);
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
