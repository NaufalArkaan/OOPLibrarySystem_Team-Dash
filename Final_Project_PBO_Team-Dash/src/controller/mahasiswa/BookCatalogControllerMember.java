package controller.mahasiswa;

import Data.Book;
import SQL_DATA.BookDAO;
import controller.model.SharedBookData;
import controller.util.mahasiswa.NavigationHelper;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class BookCatalogControllerMember {

    @FXML private Button btnLeft, btnRight;
    @FXML private TextField searchField;
    @FXML private FlowPane bookContainer;

    private int currentIndex = 0;
    private final int displayCount = 4;

    private List<Book> allBooks = new ArrayList<>();
    private List<Book> filteredBooks = new ArrayList<>();
    private final BookDAO bookDAO = new BookDAO();

    @FXML
    public void initialize() {
        allBooks = bookDAO.getAllBooks();
        filteredBooks = new ArrayList<>(allBooks);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> performSearch(newVal));
        btnLeft.setOnAction(e -> navigateBooks(-1));
        btnRight.setOnAction(e -> navigateBooks(1));
        updateBookDisplay();
    }

    private void navigateBooks(int direction) {
        int newIndex = currentIndex + direction * displayCount;
        if (newIndex >= 0 && newIndex < filteredBooks.size()) {
            currentIndex = newIndex;
        }
        updateBookDisplay();
    }

    private void performSearch(String keywordRaw) {
        String keyword = keywordRaw.trim().toLowerCase();
        if (keyword.isEmpty()) {
            filteredBooks = new ArrayList<>(allBooks);
        } else {
            filteredBooks = allBooks.stream()
                    .filter(book ->
                            book.getTitle().toLowerCase().contains(keyword) ||
                                    book.getAuthor().toLowerCase().contains(keyword) ||
                                    book.getCode().toLowerCase().contains(keyword)
                    )
                    .collect(Collectors.toList());
        }
        currentIndex = 0;
        updateBookDisplay();
    }

    private void updateBookDisplay() {
        bookContainer.getChildren().clear();
        if (filteredBooks.isEmpty()) {
            Label noResult = new Label("❌ Buku tidak ditemukan.");
            noResult.setStyle("-fx-font-size: 18px; -fx-text-fill: #888;");
            bookContainer.getChildren().add(noResult);
            btnLeft.setDisable(true);
            btnRight.setDisable(true);
            return;
        }

        btnLeft.setDisable(currentIndex == 0);
        btnRight.setDisable(currentIndex + displayCount >= filteredBooks.size());

        int end = Math.min(currentIndex + displayCount, filteredBooks.size());
        for (int i = currentIndex; i < end; i++) {
            Book book = filteredBooks.get(i);
            bookContainer.getChildren().add(createBookBox(book));
        }
    }

    private VBox createBookBox(Book book) {
        VBox bookBox = new VBox(10);
        bookBox.setStyle("-fx-alignment: center; -fx-padding: 10; -fx-background-color: white; " +
                "-fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5;");
        bookBox.setPrefWidth(140);

        ImageView iv = new ImageView();
        iv.setFitWidth(100);
        iv.setFitHeight(150);
        iv.setPreserveRatio(true);

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setPrefSize(50, 50);
        StackPane imageContainer = new StackPane(progressIndicator, iv);
        imageContainer.setPrefHeight(150);

        Task<Image> loadImageTask = new Task<>() {
            @Override
            protected Image call() {
                byte[] imageData = bookDAO.getImageByBookCode(book.getCode());
                if (imageData != null && imageData.length > 0) {
                    return new Image(new ByteArrayInputStream(imageData));
                }
                return null;
            }
        };
        loadImageTask.setOnSucceeded(event -> {
            iv.setImage(loadImageTask.getValue());
            progressIndicator.setVisible(false);
        });
        loadImageTask.setOnFailed(event -> progressIndicator.setVisible(false));
        new Thread(loadImageTask).start();

        Label titleLabel = new Label(book.getTitle());
        titleLabel.setWrapText(true);
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label authorLabel = new Label("Author: " + book.getAuthor());
        authorLabel.setWrapText(true);
        authorLabel.setTextAlignment(TextAlignment.CENTER);
        authorLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");

        Label isbnLabel = new Label("ISBN: " + book.getCode());
        isbnLabel.setWrapText(true);
        isbnLabel.setTextAlignment(TextAlignment.CENTER);
        isbnLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");

        Label stockLabel = new Label("Stok: " + book.getQuantity());
        stockLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        stockLabel.setTextAlignment(TextAlignment.CENTER);

        Button borrowBtn = new Button("✅ Borrow");
        borrowBtn.setOnAction(e -> {
            SharedBookData.setSelectedBook(book);
            AnchorPane mainContentPane = (AnchorPane) bookContainer.getScene().lookup("#contentPane");
            if (mainContentPane != null) {
                NavigationHelper.navigate(mainContentPane, "borrowBtn");
            }
        });

        if (book.getQuantity() <= 0) {
            borrowBtn.setDisable(true);
            borrowBtn.setText("Stok Habis");
            borrowBtn.setStyle("-fx-background-color: grey; -fx-text-fill: white;");
        } else {
            borrowBtn.setDisable(false);
            borrowBtn.setText("✅ Borrow");
            borrowBtn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        }

        bookBox.getChildren().addAll(imageContainer, titleLabel, authorLabel, isbnLabel, stockLabel, borrowBtn);
        return bookBox;
    }
}