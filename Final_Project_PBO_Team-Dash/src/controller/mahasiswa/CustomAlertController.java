package controller.mahasiswa;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CustomAlertController {

    @FXML
    private Label messageLabel;

    @FXML
    private Button okButton;


    public void setMessage(String message) {
        messageLabel.setText(message);
    }

   
    @FXML
    private void handleOK() {
        // Mendapatkan stage dari tombol dan menutupnya
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
}