package controller.mahasiswa;

import SQL_DATA.UserDAO; // <-- Ganti import dari UserDatabase ke UserDAO
import User.Member; // <-- Import kelas Member
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

    // Buat instance dari UserDAO
    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String nim = nimField.getText().trim();
        String major = majorField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || nim.isEmpty() || major.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Registrasi Gagal", "Semua field harus diisi!");
            return;
        }

        // PERBAIKAN: Buat objek Member baru dari data form
        // Kita menggunakan konstruktor kedua di Member yang tidak memerlukan userId
        Member newMember = new Member(username, password, username, email, major, nim); // Nama disamakan dengan username untuk sementara

        // PERBAIKAN: Panggil metode registerMember dari UserDAO
        if (userDAO.registerMember(newMember)) {
            showAlert(Alert.AlertType.INFORMATION, "Registrasi Berhasil", "Akun berhasil dibuat! Silakan login.");
            goToLoginScene();
        } else {
            showAlert(Alert.AlertType.ERROR, "Registrasi Gagal", "Username '" + username + "' sudah digunakan. Silakan coba yang lain.");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        goToLoginScene();
    }

    private void goToLoginScene() {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Scene loginScene = new Scene(loginPage, 1200, 600);
            Main.getPrimaryStage().setScene(loginScene);
            Main.getPrimaryStage().setTitle("Login - UMM Library Access");
            Main.getPrimaryStage().setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}