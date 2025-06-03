package controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

        if (username.equals("admin") && password.equals("admin123")) {
            System.out.println("Admin login sukses!");
        } else {
            System.out.println("Admin login gagal!");
        }
    }

    @FXML
    private void handleMemberLoginLink(ActionEvent event) {
        try {
            Parent memberLoginPage = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Main.mainScene.setRoot(memberLoginPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
