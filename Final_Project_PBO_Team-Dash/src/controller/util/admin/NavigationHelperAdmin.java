package controller.util.admin;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import main.Main;

import java.io.IOException;

public class NavigationHelperAdmin {

    /**
     * Navigasi antar konten (dalam satu scene) berdasarkan button ID.
     *
     * @param contentPane Pane target yang ingin diisi ulang kontennya
     * @param buttonId    fx:id dari tombol yang ditekan
     */
    public static void navigate(AnchorPane contentPane, String buttonId) {
        String fxmlFile;

        switch (buttonId) {
            case "homeBtn":
                fxmlFile = "/view/admin/Dashboard.fxml";
                break;
            case "booksBtn":
                fxmlFile = "/view/admin/ManageBooks.fxml";
                break;
            case "returnBtn":
                fxmlFile = "/view/admin/ReturnBooks.fxml";
                break;
            case "reportBtn":
                fxmlFile = "/view/admin/ReportsAndCharts.fxml";
                break;
            default:
                System.err.println("[ADMIN NAVIGATION] Unknown button id: " + buttonId);
                return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(NavigationHelperAdmin.class.getResource(fxmlFile));
            Parent newContent = loader.load();

            // Bersihkan children lalu tambahkan new content
            contentPane.getChildren().clear();
            contentPane.getChildren().add(newContent);

            // Set anchor agar memenuhi area contentPane
            AnchorPane.setTopAnchor(newContent, 0.0);
            AnchorPane.setRightAnchor(newContent, 0.0);
            AnchorPane.setBottomAnchor(newContent, 0.0);
            AnchorPane.setLeftAnchor(newContent, 0.0);

        } catch (IOException e) {
            System.err.println("[ADMIN NAVIGATION] Failed to load FXML: " + fxmlFile);
            e.printStackTrace();
        }
    }

    /**
     * Load ulang scene login saat logout.
     */
    public static void loadLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationHelperAdmin.class.getResource("/view/admin/LoginAdmin.fxml"));
            Parent loginRoot = loader.load();

            Scene loginScene = new Scene(loginRoot, 1200, 600);
            var stage = Main.getPrimaryStage();

            stage.setScene(loginScene);
            stage.setTitle("Login - UMM Library Access (Admin)");
            stage.setMinWidth(900);
            stage.setMinHeight(600);
            stage.setMaximized(true);

        } catch (IOException e) {
            System.err.println("[ADMIN NAVIGATION] Failed to load LoginAdmin.fxml during logout");
            e.printStackTrace();
        }
    }
}
