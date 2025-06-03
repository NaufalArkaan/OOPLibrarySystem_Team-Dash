package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import main.Main;
import java.io.IOException;
import java.net.URL;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("user") && password.equals("1234")) {
            System.out.println("Login sukses!");
        } else {
            System.out.println("Login gagal!");
        }
    }

    @FXML
    private void handleSingUpLink(ActionEvent event) {
        try {
            Parent registerPage = FXMLLoader.load(getClass().getResource("/view/Register.fxml"));
            Main.mainScene.setRoot(registerPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdminLoginLink(ActionEvent event) {
        try {
            Parent adminLoginPage = FXMLLoader.load(getClass().getResource("/view/LoginAdmin.fxml"));
            Main.mainScene.setRoot(adminLoginPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals("user") && password.equals("1234")) {
            System.out.println("Login sukses!");
            try {
                URL dashboardURL = getClass().getResource("/view/ViewControll.fxml");
                System.out.println("Dashboard.fxml URL: " + dashboardURL);

                if (dashboardURL == null) {
                    System.out.println("ERROR: Dashboard.fxml tidak ditemukan!");
                    return;
                }

                Parent dashboardRoot = FXMLLoader.load(dashboardURL);
                Main.mainScene.setRoot(dashboardRoot);
                Main.primaryStage.setTitle("Dashboard - UMM Library Access");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Login gagal!");
        }
    }


}

