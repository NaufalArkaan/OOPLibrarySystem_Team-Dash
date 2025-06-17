package controller.admin;

import Data.Book;
import SQL_DATA.BookDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AddBookController {

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField categoryField;
    @FXML private TextField isbnField;
    @FXML private TextField quantityField; // <-- PERUBAHAN: Dari Spinner menjadi TextField
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Button addImageButton;

    private Book bookToEdit = null;
    private boolean isEditing = false;
    private final BookDAO bookDAO = new BookDAO();
    private byte[] imageData;

    @FXML
    public void initialize() {
        // Tidak perlu lagi setup untuk Spinner
        saveButton.setOnAction(e -> saveBook());
        cancelButton.setOnAction(e -> closeWindow());
        addImageButton.setOnAction(e -> handleAddImage());
    }

    public void setBookToEdit(Book book) {
        this.bookToEdit = book;
        this.isEditing = true;

        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        categoryField.setText(book.getCategory());
        isbnField.setText(book.getCode());
        isbnField.setEditable(false);
        this.imageData = book.getImage();

        // PERUBAHAN: Mengatur teks pada quantityField
        quantityField.setText(String.valueOf(book.getQuantity()));
    }

    private void saveBook() {
        String title = titleField.getText();
        String author = authorField.getText();
        String category = categoryField.getText();
        String isbn = isbnField.getText();
        int quantity;

        // PERUBAHAN: Mengambil nilai dari TextField dan mengubahnya menjadi angka
        try {
            quantity = Integer.parseInt(quantityField.getText());
            if (quantity < 0) {
                showAlert(Alert.AlertType.ERROR, "Input Salah", "Kuantitas tidak boleh negatif.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Salah", "Harap masukkan angka yang valid untuk kuantitas.");
            return;
        }

        if (title.isEmpty() || author.isEmpty() || category.isEmpty() || isbn.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Semua field teks harus diisi!");
            return;
        }

        String status = (quantity > 0) ? "Available" : "Borrowed";

        if (isEditing) {
            Book updatedBook = new Book(bookToEdit.getCode(), title, author, category, this.imageData, status, quantity);
            boolean success = bookDAO.updateBook(updatedBook);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data buku berhasil diperbarui.");
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal memperbarui data buku.");
            }
        } else {
            Book newBook = new Book(isbn, title, author, category, this.imageData, status, quantity);
            boolean success = bookDAO.addBook(newBook);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Buku baru berhasil ditambahkan.");
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menambahkan buku baru. Mungkin ISBN sudah ada.");
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    private void handleAddImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Gambar Buku");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(addImageButton.getScene().getWindow());

        if (selectedFile != null) {
            try {
                this.imageData = Files.readAllBytes(selectedFile.toPath());
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Gambar berhasil dipilih: " + selectedFile.getName());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Gagal", "Tidak dapat membaca file gambar.");
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}