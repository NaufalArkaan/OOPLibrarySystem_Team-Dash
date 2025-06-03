package controller.mahasiswa;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import main.Main;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField nimField;

    @FXML
    private TextField majorField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String nim = nimField.getText();
        String major = majorField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || nim.isEmpty() || major.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registrasi Gagal");
            alert.setHeaderText(null);
            alert.setContentText("Semua field harus diisi!");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registrasi Berhasil");
        alert.setHeaderText(null);
        alert.setContentText("Akun berhasil dibuat! Silakan login.");
        alert.showAndWait();

        // Pindah ke halaman login
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Main.mainScene.setRoot(loginPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleCancel(ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Main.mainScene.setRoot(loginPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
