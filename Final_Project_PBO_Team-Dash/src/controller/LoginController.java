package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import main.Main;
import controller.util.mahasiswa.UserDatabase;
import java.io.IOException;
import java.net.URL;

public class LoginController {

    @FXML private Hyperlink signUpLink;
    @FXML private Hyperlink adminLoginLink;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleSignUpLink(ActionEvent event) {
        loadScene("/view/mahasiswa/Register.fxml", "Register - UMM Library Access", 1200, 600);
    }

    @FXML
    private void handleAdminLoginLink(ActionEvent event) {
        loadScene("/view/admin/LoginAdmin.fxml", "Admin Login - UMM Library Access", 1200, 600);
        Main.getPrimaryStage().setMinWidth(900);
        Main.getPrimaryStage().setMinHeight(600);
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (UserDatabase.isValidUser(username, password)) {
            System.out.println("Login sukses!");

            // Jangan langsung load DashboardMember.fxml
            // Tapi load MainLayout.fxml yang punya navbar dan content pane
            loadScene("/view/mahasiswa/ViewControll.fxml", "Dashboard - UMM Library Access", 1200, 800);
            Main.getPrimaryStage().setMaximized(true);

        } else {
            System.out.println("Login gagal!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Gagal");
            alert.setHeaderText(null);
            alert.setContentText("Username atau password salah!");
            alert.showAndWait();
        }
    }

    private void loadScene(String fxmlPath, String title, double width, double height) {
        try {
            URL resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                System.err.println("ERROR: FXML file not found: " + fxmlPath);
                return;
            }
            Parent root = FXMLLoader.load(resource);
            Scene scene = new Scene(root, width, height);
            Main.getPrimaryStage().setScene(scene);
            Main.getPrimaryStage().setTitle(title);
            Main.getPrimaryStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
