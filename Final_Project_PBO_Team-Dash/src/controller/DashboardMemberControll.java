package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.Parent;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class DashboardMemberControll {

    @FXML
    private Button homeBtn, booksBtn, borrowBtn, historyBtn, logoutBtn;

    @FXML
    private ListView<String> borrowedBooksList;

    @FXML
    private ListView<String> returnBook;

    @FXML
    private BorderPane rootPane; // Tambahkan fx:id="rootPane" di BorderPane FXML untuk referensi ini

    @FXML
    public void initialize() {
        List<String> borrowedBooks = Arrays.asList(
                "1. Laskar Pelangi - Andrea Hirata",
                "2. Bumi Manusia - Pramoedya Ananta Toer",
                "3. Filosofi Teras - Henry Manampiring"
        );
        borrowedBooksList.getItems().addAll(borrowedBooks);

        List<String> booksToReturn = Arrays.asList(
                "1. Atomic Habits - James Clear",
                "2. Rich Dad Poor Dad - Robert Kiyosaki"
        );
        returnBook.getItems().addAll(booksToReturn);

        // Set aksi tombol
        homeBtn.setOnAction(this::handleNavigation);
        booksBtn.setOnAction(this::handleNavigation);
        borrowBtn.setOnAction(this::handleNavigation);
        historyBtn.setOnAction(this::handleNavigation);
        logoutBtn.setOnAction(this::handleNavigation);
    }

    private void handleNavigation(ActionEvent event) {
        Button clickedBtn = (Button) event.getSource();
        String fxmlFile = "";

        switch (clickedBtn.getId()) {
            case "homeBtn":
                fxmlFile = "/view/Home.fxml";
                break;
            case "booksBtn":
                fxmlFile = "/view/Books.fxml";
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
        }

        if (!fxmlFile.isEmpty()) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
                // Asumsikan rootPane adalah BorderPane utama di DashboardMember
                rootPane.getScene().setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
