package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.Main;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookCatalogControllerMember {

    @FXML
    private TextField searchField;

    @FXML
    private FlowPane bookFlowPane;

    @FXML
    private Button btnHome, btnBooks, btnBorrow, btnHistory, btnLogout;

    private final List<Book> allBooks = new ArrayList<>();

    @FXML
    public void initialize() {
        // Data buku dummy
        allBooks.add(new Book("Otodidak Desain dan Pemrograman Website", "Jubilee Enterprise", "0000", "Fiction", "/images/book1.png"));
        allBooks.add(new Book("Sejarah Dunia Lengkap", "Hutton Webster", "0001", "Non-Fiction", "/images/book2.png"));
        allBooks.add(new Book("Pulang", "Tere Liye", "0002", "Non-Fiction", "/images/book3.png"));
        allBooks.add(new Book("Hukum Pidana", "Dr.H. Iskandar, S.H., M.Hum.", "0003", "Fiction", "/images/book4.png"));

        // Event pencarian
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterBooks(newVal));

        // Navigasi - ganti scene via Main.setCenterContent()
        btnHome.setOnAction(e -> Main.setCenterContent("/view/dashboardMember.fxml"));
        btnBooks.setOnAction(e -> Main.setCenterContent("/view/bookCatalogMember.fxml"));
        btnBorrow.setOnAction(e -> Main.setCenterContent("/view/borrow.fxml"));
        btnHistory.setOnAction(e -> Main.setCenterContent("/view/history.fxml"));
        btnLogout.setOnAction(e -> {
            // Untuk logout bisa tutup stage & buka login, atau ganti scene di Main sesuai desainmu
            System.out.println("Logout clicked, implement sesuai kebutuhan");
        });

        // Tampilkan semua buku di awal
        displayBooks(allBooks);
    }

    private void filterBooks(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            displayBooks(allBooks);
        } else {
            List<Book> filtered = allBooks.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
            displayBooks(filtered);
        }
    }

    private void displayBooks(List<Book> books) {
        bookFlowPane.getChildren().clear();
        for (Book book : books) {
            VBox card = createBookCard(book);
            bookFlowPane.getChildren().add(card);
        }
    }

    private VBox createBookCard(Book book) {
        VBox vbox = new VBox(5);
        vbox.setStyle("""
                -fx-padding: 10;
                -fx-background-color: #ffffff;
                -fx-border-color: #ccc;
                -fx-border-radius: 8;
                -fx-background-radius: 8;
                """);
        vbox.setPrefWidth(145);

        // Gambar buku
        ImageView imageView = new ImageView();
        InputStream imageStream = getClass().getResourceAsStream(book.getImagePath());
        if (imageStream != null) {
            imageView.setImage(new Image(imageStream));
        } else {
            System.err.println("Gagal memuat gambar: " + book.getImagePath());
        }
        imageView.setFitWidth(145);
        imageView.setFitHeight(226);

        // Info buku
        Text title = new Text(book.getTitle());
        title.setStyle("-fx-font-weight: bold;");
        Text author = new Text("Author: " + book.getAuthor());
        Text isbn = new Text("ISBN: " + book.getIsbn());
        Text category = new Text("Category: " + book.getCategory());

        // Tombol pinjam
        Button borrowBtn = new Button("Borrow");
        borrowBtn.setStyle("""
                -fx-background-color: #4CAF50;
                -fx-text-fill: white;
                -fx-background-radius: 8;
                """);
        borrowBtn.setOnAction(e -> showBorrowSuccessDialog(book.getTitle()));

        vbox.getChildren().addAll(imageView, title, author, isbn, category, borrowBtn);
        return vbox;
    }

    private void showBorrowSuccessDialog(String bookTitle) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Peminjaman Berhasil");
        alert.setHeaderText(null);
        alert.setContentText("Buku \"" + bookTitle + "\" berhasil dipinjam!");
        alert.showAndWait();
    }

    // Inner class Book tetap sama
    public static class Book {
        private final String title, author, isbn, category, imagePath;

        public Book(String title, String author, String isbn, String category, String imagePath) {
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.category = category;
            this.imagePath = imagePath;
        }

        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getIsbn() { return isbn; }
        public String getCategory() { return category; }
        public String getImagePath() { return imagePath; }
    }
}
