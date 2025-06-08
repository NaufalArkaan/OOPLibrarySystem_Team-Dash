package controller.util.mahasiswa;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import main.Main;

import java.io.IOException;

public class NavigationHelper {

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
                fxmlFile = "/view/mahasiswa/DashboardMember.fxml";
                break;
            case "booksBtn":
                fxmlFile = "/view/mahasiswa/BookCatalogMember.fxml";
                break;
            case "borrowBtn":
                fxmlFile = "/view/mahasiswa/BorrowBook.fxml";
                break;
            case "historyBtn":
                fxmlFile = "/view/mahasiswa/History.fxml";
                break;
            default:
                System.err.println("[NAVIGATION] Unknown button id: " + buttonId);
                return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(NavigationHelper.class.getResource(fxmlFile));
            Parent newContent = loader.load();

            // Bersihkan dulu children
            contentPane.getChildren().clear();

            // Tambahkan newContent ke contentPane
            contentPane.getChildren().add(newContent);

            // Set anchor agar newContent memenuhi contentPane
            AnchorPane.setTopAnchor(newContent, 0.0);
            AnchorPane.setRightAnchor(newContent, 0.0);
            AnchorPane.setBottomAnchor(newContent, 0.0);
            AnchorPane.setLeftAnchor(newContent, 0.0);

        } catch (IOException e) {
            System.err.println("[NAVIGATION] Failed to load FXML: " + fxmlFile);
            e.printStackTrace();
        }
    }

    /**
     * Load ulang scene Login (untuk logout).
     *
     * NOTE: Sebaiknya logout di-handle di controller utama agar lebih terstruktur.
     */
    public static void loadLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationHelper.class.getResource("/view/Login.fxml"));
            Parent loginRoot = loader.load();

            Scene loginScene = new Scene(loginRoot, 1200, 600);

            var stage = Main.getPrimaryStage();
            stage.setScene(loginScene);
            stage.setTitle("Login - UMM Library Access");
            stage.setMinWidth(900);
            stage.setMinHeight(600);
            stage.setMaximized(true);

        } catch (IOException e) {
            System.err.println("[NAVIGATION] Failed to load Login.fxml during logout");
            e.printStackTrace();
        }
    }
}
