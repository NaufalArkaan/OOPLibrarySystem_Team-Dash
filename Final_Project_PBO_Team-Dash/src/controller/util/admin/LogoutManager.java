package controller.util.admin;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;

public class LogoutManager {

    public static void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(LogoutManager.class.getResource("/view/LoginAdminControl.fxml"));
            Parent root = loader.load();

            Stage stage = Main.getPrimaryStage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Gagal kembali ke halaman login.");
            e.printStackTrace();
        }
    }
}
