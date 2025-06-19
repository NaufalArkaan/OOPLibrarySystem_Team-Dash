package controller.util.mahasiswa;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import main.Main;

import java.io.IOException;

public class ControllView {

    @FXML
    private Button homeBtn, booksBtn, borrowBtn, historyBtn, logoutBtn;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private BorderPane rootPane;

    @FXML
    public void initialize() {
        if (homeBtn == null || contentPane == null) {
            System.err.println("FXML elements not injected properly!");
            return;
        }

        homeBtn.setOnAction(this::handleNavigation);
        booksBtn.setOnAction(this::handleNavigation);
        borrowBtn.setOnAction(this::handleNavigation);
        historyBtn.setOnAction(this::handleNavigation);
        logoutBtn.setOnAction(this::handleNavigation);

        NavigationHelper.navigate(contentPane, homeBtn.getId());
    }

    @FXML
    private void handleNavigation(ActionEvent event) {
        if (!(event.getSource() instanceof Button)) return;

        Button clickedBtn = (Button) event.getSource();
        String buttonId = clickedBtn.getId();

        if ("logoutBtn".equals(buttonId)) {
            showLogoutConfirmation();
        } else {
            NavigationHelper.navigate(contentPane, buttonId);
        }
    }

    private void showLogoutConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Logout");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin logout?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                goToLogin();
            }
        });
    }

    private void goToLogin() {
        System.out.println("goToLogin() called!");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent loginRoot = loader.load();

            System.out.println("Login.fxml loaded!");

            Scene loginScene = new Scene(loginRoot, 1200, 600);

            var stage = Main.getPrimaryStage();

            stage.setMaximized(false);
            stage.setScene(loginScene);
            stage.setTitle("UMM Library Access");
            stage.setMinWidth(1200);
            stage.setMinHeight(600);

            PauseTransition maximizeDelay = new PauseTransition(Duration.millis(200));
            maximizeDelay.setOnFinished(e -> stage.setMaximized(true));
            maximizeDelay.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}