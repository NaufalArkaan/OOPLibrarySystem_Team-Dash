package controller.mahasiswa;

import controller.util.mahasiswa.UserDatabase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import main.Main;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private TextField nimField;
    @FXML private TextField majorField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String nim = nimField.getText().trim();
        String major = majorField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || nim.isEmpty() || major.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Registrasi Gagal", null, "Semua field harus diisi!");
            return;
        }

        if (UserDatabase.userExists(username)) {
            showAlert(Alert.AlertType.ERROR, "Registrasi Gagal", null, "Username sudah digunakan, coba yang lain.");
            return;
        }

        UserDatabase.addUser(username, password);

        showAlert(Alert.AlertType.INFORMATION, "Registrasi Berhasil", null, "Akun berhasil dibuat! Silakan login.");

        goToLoginScene();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        goToLoginScene();
    }

    private void goToLoginScene() {
        try {
            System.out.println("Loading Login.fxml...");
            Parent loginPage = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            System.out.println("Loaded Login.fxml");
            Scene loginScene = new Scene(loginPage, 1200, 600);

            Main.getPrimaryStage().setScene(loginScene);

            // Maksimalkan window supaya fullscreen (optional)
            Main.getPrimaryStage().setMaximized(true);

            Main.getPrimaryStage().setTitle("Login - UMM Library Access");
            System.out.println("Switched to login scene.");

        } catch (IOException e) {
            System.err.println("Error loading Login.fxml:");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
