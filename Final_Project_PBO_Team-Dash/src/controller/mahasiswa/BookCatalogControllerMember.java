package controller.mahasiswa;

import controller.model.SharedBookData;
import controller.model.Book;
import controller.util.mahasiswa.NavigationHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.stream.Collectors;

public class BookCatalogControllerMember {

    @FXML private Button btnLeft, btnRight;
    @FXML private TextField searchField;
    @FXML private FlowPane bookContainer;
    @FXML private AnchorPane contentPane; // Tambahkan ini untuk navigasi

    private int currentIndex = 0;
    private final int displayCount = 3;

    private List<Book> filteredBooks;

    @FXML
    public void initialize() {
        filteredBooks = SharedBookData.getBooks();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> performSearch(newVal));

        btnLeft.setOnAction(e -> {
            if (currentIndex - displayCount >= 0) {
                currentIndex -= displayCount;
                updateBookDisplay();
            }
        });

        btnRight.setOnAction(e -> {
            if (currentIndex + displayCount < filteredBooks.size()) {
                currentIndex += displayCount;
                updateBookDisplay();
            }
        });

        updateBookDisplay();
    }

    private void performSearch(String keywordRaw) {
        String keyword = keywordRaw.trim().toLowerCase();
        filteredBooks = SharedBookData.getBooks().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(keyword) ||
                        book.getAuthor().toLowerCase().contains(keyword) ||
                        book.getIsbn().toLowerCase().contains(keyword))
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
                Image img = new Image(getClass().getResourceAsStream("/view/images/" + book.getImagePath()));
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
            Label isbnLabel = new Label("ISBN: " + book.getIsbn());
            Label categoryLabel = new Label("Category: " + book.getCategory());
            Label statusLabel = new Label("Status: " + book.getStatus());

            authorLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
            isbnLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
            categoryLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");
            statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");

            Button borrowBtn = new Button("✅ Available");
            borrowBtn.setStyle("-fx-background-color: green; -fx-text-fill: white;");

            if (book.getStatus().equalsIgnoreCase("Borrowed")) {
                borrowBtn.setDisable(true);
                borrowBtn.setText("⛔ Borrowed");
                borrowBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            } else {
                borrowBtn.setOnAction(e -> {
                    SharedBookData.setSelectedBook(book); // <<--- Tambahkan ini
                    NavigationHelper.navigate(contentPane, "borrowBtn"); // Navigasi setelah set data
                });
            }

            bookBox.getChildren().addAll(iv, titleLabel, authorLabel, isbnLabel, categoryLabel, statusLabel, borrowBtn);
            bookContainer.getChildren().add(bookBox);
        }
    }
}
