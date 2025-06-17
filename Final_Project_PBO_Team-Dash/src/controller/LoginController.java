// File: src/controller/LoginController.java

package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import main.Main;
import SQL_DATA.UserDAO;
import User.Admin;
import User.Member;
import User.User;
import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private UserDAO userDAO = new UserDAO();
    public static User loggedInUser; // Variabel ini akan kita gunakan

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Gagal", "Username dan password harus diisi.");
            return;
        }

        loggedInUser = userDAO.findUserByCredentials(username, password);

        if (loggedInUser != null) {
            System.out.println("Login berhasil!");
            if (loggedInUser instanceof Admin) {
                try {
                    Main.showAdminView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (loggedInUser instanceof Member) {
                try {
                    // CUKUP MUAT FXML-NYA SAJA, TANPA LOGIKA CONTROLLER TAMBAHAN
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/mahasiswa/ViewControll.fxml"));
                    Parent root = loader.load();

                    Scene scene = new Scene(root, 1200, 800);
                    Main.getPrimaryStage().setScene(scene);
                    Main.getPrimaryStage().setTitle("Dashboard Member");
                    Main.getPrimaryStage().setMaximized(true);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Gagal", "Username atau password salah!");
        }
    }

    // Metode lainnya tetap sama...
    @FXML
    private void handleSignUpLink(ActionEvent event) {
        try {
            Main.showRegistrationView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdminLoginLink(ActionEvent event) {
        try {
            Main.showAdminLoginView();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal memuat halaman login admin.");
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