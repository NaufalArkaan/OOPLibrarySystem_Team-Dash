package controller.util.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;

public class AdminLayoutController {

    @FXML
    private AnchorPane contentPane;

    @FXML
    private Button homeBtn, booksBtn, returnBtn, reportBtn, logoutBtn;

    /**
     * Menangani navigasi tombol selain Logout.
     */
    @FXML
    private void handleNavigation(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();

        if ("logoutBtn".equals(buttonId)) {
            handleLogout();
        } else {
            NavigationHelperAdmin.navigate(contentPane, buttonId);
        }
    }

    /**
     * Logout dan kembali ke halaman login admin.
     */
    private void handleLogout() {
        NavigationHelperAdmin.loadLoginScene();
    }

    /**
     * Inisialisasi default view saat admin login.
     */
    @FXML
    private void initialize() {
        NavigationHelperAdmin.navigate(contentPane, "homeBtn");
    }
}
