package controller.admin;

import controller.model.ReturnRecord;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddFineController {

    @FXML private TextField nameField;
    @FXML private TextField studentIdField;
    @FXML private TextField fineField;
    @FXML private Button cancelButton;
    @FXML private Button submitButton;

    private ReturnRecord selectedRecord;

    public void setData(ReturnRecord record) {
        this.selectedRecord = record;
        if (record != null) {
            nameField.setText(record.getMemberName());
            studentIdField.setText(record.getStudentId());
            fineField.setText(record.getFine());
        }
    }

    @FXML
    public void initialize() {
        // Hanya untuk tampilan, tidak boleh diedit
        nameField.setEditable(false);
        studentIdField.setEditable(false);

        cancelButton.setOnAction(e -> closeDialog());

        submitButton.setOnAction(e -> {
            String fineAmount = fineField.getText().trim();

            if (selectedRecord == null) {
                showAlert("Error", "Tidak ada data yang dipilih.");
                return;
            }

            if (fineAmount.isEmpty()) {
                showAlert("Validasi Gagal", "Masukkan jumlah denda terlebih dahulu.");
                return;
            }

            if (!fineAmount.matches("\\d+(\\.\\d{1,2})?")) {
                showAlert("Validasi Gagal", "Jumlah denda harus berupa angka.");
                return;
            }

            selectedRecord.setFine(fineAmount);
//            selectedRecord.setStatus("Denda Ditambahkan");

            closeDialog();
        });
    }

    private void closeDialog() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
