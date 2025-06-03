package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class NavigationHelper {

    public static void navigate(AnchorPane contentPane, String buttonId) {
        String fxmlFile;

        switch (buttonId) {
            case "homeBtn":
                fxmlFile = "/view/DashboardMember.fxml";
                break;
            case "booksBtn":
                fxmlFile = "/view/BookCatalogMember.fxml";
                break;
            case "borrowBtn":
                fxmlFile = "/view/BorrowBook.fxml";
                break;
            case "historyBtn":
                fxmlFile = "/view/History.fxml";
                break;
            case "logoutBtn":
                fxmlFile = "/view/Login.fxml";
                break;
            default:
                System.err.println("Unknown button id: " + buttonId);
                return;
        }

        try {
            Parent newContent = FXMLLoader.load(NavigationHelper.class.getResource(fxmlFile));
            contentPane.getChildren().setAll(newContent);  // Replace content
            AnchorPane.setTopAnchor(newContent, 0.0);
            AnchorPane.setRightAnchor(newContent, 0.0);
            AnchorPane.setBottomAnchor(newContent, 0.0);
            AnchorPane.setLeftAnchor(newContent, 0.0);
        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + fxmlFile);
            e.printStackTrace();
        }
    }
}
