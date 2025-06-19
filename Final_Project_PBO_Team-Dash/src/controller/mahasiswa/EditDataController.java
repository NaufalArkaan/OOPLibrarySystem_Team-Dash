package controller.mahasiswa;

import model.HistoryRecord;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditDataController {

    @FXML private TextField nameField;
    @FXML private TextField classField;
    @FXML private TextField titleField;
    @FXML private TextField isbnField;
    @FXML private Button updateButton;
    @FXML private Button cancelButton;

    private HistoryRecord currentRecord;

    public void setData(HistoryRecord record) {
        this.currentRecord = record;
        nameField.setText(record.getName());
        classField.setText(record.getClassName());
        titleField.setText(record.getTitle());
        isbnField.setText(record.getIsbn());
    }

    @FXML
    private void initialize() {
        updateButton.setOnAction(e -> handleUpdate());
        cancelButton.setOnAction(e -> closeWindow());
    }

    private void handleUpdate() {
        if (currentRecord != null) {
            currentRecord.setName(nameField.getText());
            currentRecord.setClassName(classField.getText());
            currentRecord.setTitle(titleField.getText());
            currentRecord.setIsbn(isbnField.getText());


            closeWindow();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
