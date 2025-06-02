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
    private BorderPane rootPane;

    @FXML
    public void initialize() {
        // Isi ListView dengan data awal
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

        // Set aksi tombol navigasi
        homeBtn.setOnAction(this::handleNavigation);
        booksBtn.setOnAction(this::handleNavigation);
        borrowBtn.setOnAction(this::handleNavigation);
        historyBtn.setOnAction(this::handleNavigation);
        logoutBtn.setOnAction(this::handleNavigation);
    }

    @FXML
    private void handleNavigation(ActionEvent event) {
        if (!(event.getSource() instanceof Button)) return;

        Button clickedBtn = (Button) event.getSource();
        String fxmlFile = null;

        switch (clickedBtn.getId()) {
            case "homeBtn":
                fxmlFile = "/view/Home.fxml";
                break;
            case "booksBtn":
                fxmlFile = "/view/BookCatalogMember.fxml"; // pastikan file ada
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
                System.err.println("Unknown button id: " + clickedBtn.getId());
                return;
        }

        try {
            Parent newContent = FXMLLoader.load(getClass().getResource(fxmlFile));
            if ("logoutBtn".equals(clickedBtn.getId())) {
                rootPane.getScene().setRoot(newContent);
            } else {
                rootPane.setCenter(newContent);
            }
        } catch (IOException e) {
            System.err.println("Failed to load FXML: " + fxmlFile);
            e.printStackTrace();
        }
    }

}
