package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookCatalogControllerMember {

    @FXML private Button btnLeft, btnRight;
    @FXML private TextField searchField;
    @FXML private FlowPane bookContainer;

    private int currentIndex = 0;
    private final int displayCount = 3;

    private final List<Book> books = new ArrayList<>();
    private List<Book> filteredBooks = new ArrayList<>();

    @FXML
    public void initialize() {
        // Tambah buku (kategori tetap ada tapi tidak dipakai filter)
        books.add(new Book("-0000", "Otodidak Desain dan Pemrograman Website", "Jubilee Enterprise", "website.png", "Non-Fiction"));
        books.add(new Book("-0001", "World History Sejarah Dunia Lengkap", "Nathan Webster PhD", "buku.png", "Non-Fiction"));
        books.add(new Book("-0002", "Pulang", "Tere Liye", "pulang.png", "Fiction"));
        books.add(new Book("-0003", "Hukum Pidana", "Dr. H. Ishaq, S.H., M.Hum", "hukum.png", "Non-Fiction"));

        // Listener searchField (live search)
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            performSearch(newVal);
        });

        // Tombol navigasi kiri
        btnLeft.setOnAction(e -> {
            if (currentIndex - displayCount >= 0) {
                currentIndex -= displayCount;
                updateBookDisplay();
            }
        });

        // Tombol navigasi kanan
        btnRight.setOnAction(e -> {
            if (currentIndex + displayCount < filteredBooks.size()) {
                currentIndex += displayCount;
                updateBookDisplay();
            }
        });

        filteredBooks = new ArrayList<>(books);
        updateBookDisplay();
    }

    private void performSearch(String keywordRaw) {
        String keyword = keywordRaw.trim().toLowerCase();

        filteredBooks = books.stream()
                .filter(book ->
                        book.getTitle().toLowerCase().contains(keyword) ||
                                book.getAuthor().toLowerCase().contains(keyword) ||
                                book.getIsbn().toLowerCase().contains(keyword)
                )
                .collect(Collectors.toList());

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

            VBox bookBox = new VBox(10);
            bookBox.setStyle("-fx-alignment: center; -fx-padding: 10; -fx-background-color: white; " +
                    "-fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5;");
            bookBox.setPrefWidth(140);

            ImageView iv = new ImageView();
            try {
                Image img = new Image(getClass().getResourceAsStream("/resources/images/" + book.getImagePath()));
                iv.setImage(img);
                iv.setFitWidth(100);
                iv.setFitHeight(150);
                iv.setPreserveRatio(true);
            } catch (Exception e) {
                System.out.println("Gambar tidak ditemukan: " + book.getImagePath());
                iv.setImage(null);
            }

            Label titleLabel = new Label(book.getTitle());
            titleLabel.setWrapText(true);
            titleLabel.setTextAlignment(TextAlignment.CENTER);
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            Label authorLabel = new Label("Author: " + book.getAuthor());
            authorLabel.setWrapText(true);
            authorLabel.setTextAlignment(TextAlignment.CENTER);
            authorLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");

            Label isbnLabel = new Label("ISBN: " + book.getIsbn());
            isbnLabel.setWrapText(true);
            isbnLabel.setTextAlignment(TextAlignment.CENTER);
            isbnLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");

            Label categoryLabel = new Label("Category: " + book.getCategory());
            categoryLabel.setWrapText(true);
            categoryLabel.setTextAlignment(TextAlignment.CENTER);
            categoryLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");

            Button borrowBtn = new Button("✅ Borrow");
            borrowBtn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            borrowBtn.setOnAction(e -> showInfo("Berhasil meminjam buku: " + book.getTitle()));

            bookBox.getChildren().addAll(iv, titleLabel, authorLabel, isbnLabel, categoryLabel, borrowBtn);
            bookContainer.getChildren().add(bookBox);
        }
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informasi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static class Book {
        private final String isbn;
        private final String title;
        private final String author;
        private final String imagePath;
        private final String category;

        public Book(String isbn, String title, String author, String imagePath, String category) {
            this.isbn = isbn;
            this.title = title;
            this.author = author;
            this.imagePath = imagePath;
            this.category = category;
        }

        public String getIsbn() { return isbn; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getImagePath() { return imagePath; }
        public String getCategory() { return category; }
    }
}
