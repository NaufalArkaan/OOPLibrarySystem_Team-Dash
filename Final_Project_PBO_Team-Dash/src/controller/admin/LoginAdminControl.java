package controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.Main;

import java.io.IOException;

public class LoginAdminControl {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if ("admin".equals(username) && "admin123".equals(password)) {
            System.out.println("Admin login sukses!");
            try {
                Parent adminDashboard = FXMLLoader.load(getClass().getResource("/view/admin/MainLayoutAdmin.fxml"));
                Scene adminScene = new Scene(adminDashboard, 1200, 600);
                Main.getPrimaryStage().setScene(adminScene);
                Main.getPrimaryStage().setTitle("Admin Dashboard - UMM Library Access");
                Main.getPrimaryStage().setMaximized(true);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", null, "Gagal memuat halaman Dashboard Admin.");
            }
        } else {
            System.out.println("Admin login gagal!");
            showAlert(Alert.AlertType.ERROR, "Login Gagal", null, "Username atau password view.admin.admin salah!");
        }
    }

    @FXML
    private void handleMemberLoginLink(ActionEvent event) {
        try {
            Parent memberLoginPage = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Scene memberScene = new Scene(memberLoginPage, 1200, 600);
            Main.getPrimaryStage().setScene(memberScene);
            Main.getPrimaryStage().setTitle("User Login - UMM Library Access");
            Main.getPrimaryStage().setMaximized(true);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", null, "Gagal memuat halaman Login User.");
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
