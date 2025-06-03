package controller.mahasiswa;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CustomAlertController {

    @FXML private Label messageLabel;

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    @FXML
    private void handleOK() {
        ((Stage) messageLabel.getScene().getWindow()).close();
    }

    @FXML
    private void handleClose() {
        ((Stage) messageLabel.getScene().getWindow()).close();
    }
}
