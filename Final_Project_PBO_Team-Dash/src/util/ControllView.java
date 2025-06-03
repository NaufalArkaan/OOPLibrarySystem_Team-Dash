package util;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;

public class ControllView {

    @FXML
    private Button homeBtn, booksBtn, borrowBtn, historyBtn, logoutBtn;

    @FXML
    private AnchorPane contentPane;  // ganti dari BorderPane

    @FXML
    private BorderPane rootPane; // tetap diperlukan untuk scene root

    @FXML
    public void initialize() {
        // Pasang handler navigasi ke semua tombol
        homeBtn.setOnAction(this::handleNavigation);
        booksBtn.setOnAction(this::handleNavigation);
        borrowBtn.setOnAction(this::handleNavigation);
        historyBtn.setOnAction(this::handleNavigation);
        logoutBtn.setOnAction(this::handleNavigation);

        // Tampilkan tampilan default (home/dashboard)
        util.NavigationHelper.navigate(contentPane, "homeBtn");
    }

    @FXML
    private void handleNavigation(ActionEvent event) {
        if (!(event.getSource() instanceof Button)) return;

        Button clickedBtn = (Button) event.getSource();
        String buttonId = clickedBtn.getId();

        util.NavigationHelper.navigate(contentPane, buttonId);
    }
}
