package controller.mahasiswa;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class BorrowBookController {

    @FXML private TextField nameField;
    @FXML private TextField studentIdField;
    @FXML private TextField classField;
    @FXML private TextField bookTitleField;
    @FXML private TextField isbnField;

    @FXML
    private void handleSubmit() {
        String name = nameField.getText().trim();
        String id = studentIdField.getText().trim();
        String cls = classField.getText().trim();
        String book = bookTitleField.getText().trim();
        String isbn = isbnField.getText().trim();

        if (name.isEmpty() || id.isEmpty() || cls.isEmpty() || book.isEmpty() || isbn.isEmpty()) {
            showCustomAlert("Please fill in all fields!");
            return;
        }

        // TODO: Tambahkan logika penyimpanan ke database di sini

        showCustomAlert("Book borrowing submitted successfully!");
        clearForm();
    }

    @FXML
    private void handleCancel() {
        clearForm();
    }


    private void clearForm() {
        nameField.clear();
        studentIdField.clear();
        classField.clear();
        bookTitleField.clear();
        isbnField.clear();
    }

    private void showCustomAlert(String message) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mahasiswa/CustomAlertBorrow.fxml"));
            Parent root = loader.load();

            CustomAlertController controller = loader.getController();
            controller.setMessage(message);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Message");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
