package controller.mahasiswa;

import SQL_DATA.UserDAO;
import User.Member;
import ExceptionHandle.UsernameAlreadyExistsException;
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

        Member newMember = new Member(username, password, username, email, major, nim);

        try {
            if (userDAO.registerMember(newMember)) {
                showAlert(Alert.AlertType.INFORMATION, "Registrasi Berhasil", "Akun berhasil dibuat! Silakan login.");
                goToLoginScene();
            }
        } catch (UsernameAlreadyExistsException e) {
            showAlert(Alert.AlertType.ERROR, "Registrasi Gagal", e.getMessage());
        } catch (RuntimeException e) {
            showAlert(Alert.AlertType.ERROR, "Error Database", "Terjadi kesalahan koneksi database: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Registrasi Gagal", "Terjadi kesalahan tak terduga: " + e.getMessage());
            e.printStackTrace();
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
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat halaman login.");
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